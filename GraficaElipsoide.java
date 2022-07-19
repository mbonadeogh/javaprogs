//***************************************
//*** Copyright © 2022 Marcelo C. Bonadeo
//*** Permission is hereby granted, free of charge, to any person obtaining a copy
//*** of this software and associated documentation files (the "Software"), to deal
//*** in the Software without restriction, including without limitation the rights
//*** to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//*** copies of the Software, and to permit persons to whom the Software is
//*** furnished to do so, subject to the following conditions:
//*** 
//*** The above copyright notice and this permission notice shall be included in all
//*** copies or substantial portions of the Software.
//*** 
//*** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//*** IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//*** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//*** AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//*** LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//*** OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//*** SOFTWARE.
//***********************************************************
//*** El lenguaje JAVA es propiedad de Oracle Inc           *
//*** Copyright © 1995, 2022, Oracle and/or its affiliates. *
//***********************************************************
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JComponent;

public class GraficaElipsoide extends Perspectiva {
	private Color drawColor;

	public GraficaElipsoide () {
		super();
	}
	
	// pobs: Punto de vista del observador, DP_: Distancia al plano de proyección
	public GraficaElipsoide (PointR3 pobs, double DP_) {
		super(pobs, DP_);
	}

	public Color getDrawColor() {
		return drawColor;
	}

	public void setDrawColor(Color drawColor) {
		this.drawColor = drawColor;
	}

    // *** Interfaz de Forma, para englobar todos los objetos a graficar bajo un mismo tipo.
	private interface Shape {
		public int type();
	}

	// *** Clase para guardar la información de una línea a graficar.
	private static class Line implements Shape {
		final int x1; 
		final int y1;
		final int x2;
		final int y2;   
		final Color color;

		public int type() {
			return 1;
		}
		
		public Line(int x1, int y1, int x2, int y2, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}               
	}	
	
	// *** Clase para guardar la información de un poligono a graficar.
	private static class MapPolygon extends Polygon implements Shape {

		private Color fillColor;
		int[] xpoints, ypoints;

		public int type() {
			return 4;
		}
		
		public MapPolygon(int[] xpoints_, int[] ypoints_, int npoints, Color color) {
			super(xpoints_, ypoints_, npoints);
			xpoints = xpoints_;
			ypoints = ypoints_;
			this.fillColor = color;
		}

		public Color getFillColor() {
			return fillColor;
		}

		public void setFillColor(Color fillColor) {
			this.fillColor = fillColor;
		}
		
/*		public int getXCoordPoint(int nPoint) {
			if (nPoint < 1 || nPoint >5) return -10000000;
			return xpoints[nPoint-1];
		}
		
		public int getYCoordPoint(int nPoint) {
			if (nPoint < 1 || nPoint >5) return -10000000;
			return ypoints[nPoint-1];
		}*/		
	}	

	// *** Lista encadenada de formas, a mantener, para la presentación de toda la información a graficar.
	private final LinkedList<Shape> shapes = new LinkedList<Shape>(); // Hey aqui estoy la definición y creación de la lista encadenada de Formas!!!!!

	// *** Crea un objeto Line entre 2 puntos, asignandole un color y agregandolo a la Lista encadanada de Formas.
	public void addShape(int x1, int y1, int x2, int y2, Color color) {
		shapes.add(new Line(x1,y1,x2,y2, color));        
	}
	
	// Agrega un segmento desde x1,y1 hasta x2,y2 - termina invocando otro metodo que crea un objeto Line.
	public void addShape(int x1, int y1, int x2, int y2) {
		addShape(x1, y1, x2, y2, (this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}

	// *** En base a un objeto AristaR3 agrega una forma Line a la lista encadenada de formas.
	public void addShape(AristR3 ar, Color color) {
   		addShape(projectedPoint(ar.getIPoint()).x,projectedPoint(ar.getIPoint()).y,projectedPoint(ar.getEPoint()).x,projectedPoint(ar.getEPoint()).y, color);        
	}	

	public void addShape(AristR3 ar) {
		addShape(ar,(this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}
	
	// Traza aristas del triangulo.
	public int addShapes(Triangulo t, Color color) {
		addShape(t.getArist(1), color);
		addShape(t.getArist(2), color);
		addShape(t.getArist(3), color);
		return 3;
	}
	
	public int addShapes(Triangulo t) {
		return addShapes(t, (this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}
	
	// Dibuja la forma "poligono" Triangulo (rellena el interior).
	public void addShape(int x1, int x2, int x3, int y1, int y2, int y3, Color color) {
		shapes.add(new MapPolygon(new int[]{x1,x2,x3}, new int[]{y1,y2,y3}, 3, color));        
	}
	
	public void addShape(Triangulo t, Color color) {
		addShape(projectedPoint(t.getPoint(1)).x, projectedPoint(t.getPoint(2)).x, projectedPoint(t.getPoint(3)).x, projectedPoint(t.getPoint(1)).y, projectedPoint(t.getPoint(2)).y, projectedPoint(t.getPoint(3)).y, color);
	}	

	// *** Agrega formas de otra lista encadenada a la lista encadenada principal, retornando la cantidad de formas agregadas.
	public int addShapes(LinkedList<Shape> sourceShapes) {
		int srcLen = sourceShapes.size();
		for (int i = 0; i < srcLen; i++) {
			shapes.add((Shape)sourceShapes.get(i));
		}
		return srcLen;
	}
	
	// *** Remueve la primera forma de la lista encadenada.
	public Shape clearFirstShape() {
		Shape firstShape = shapes.removeFirst();
		return firstShape;
	}
	
	// Remueve las primeras N formas de la lista encadanada, retornando un array con las N formas eliminadas.
	public Shape [] clearFirstNShapes(int n) {
		Shape firstNShapes [] = new Shape [n];
		for (int i=0; i < n; i++) {
			firstNShapes[i]=clearFirstShape();
		}
		return firstNShapes;
	}	
	
	// *** Remueve la última forma de la lista encadenada.
	public Shape clearLastShape() {
		Shape lastShape = shapes.removeLast();
		return lastShape;
	}

	// *** Obtiene la última forma de la lista encadenada.
	public Shape getLastShape() {
		Shape lastShape = shapes.peekLast();
		return lastShape;
	}

	// Remueve las últimas N formas de la lista encadanada, retornando un array con las N formas eliminadas.
	public Shape [] clearLastNShapes(int n) {
		Shape lastNShapes [] = new Shape [n];
		for (int i=0; i < n; i++) {
			lastNShapes[i]=clearLastShape();
		}
		return lastNShapes;
	}

	// Metodo sobreescrito para presentar las figuras de la lista.
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		Dimension dim = getPreferredSize();
		g.fillRect(0,0,(int)dim.getWidth(),(int)dim.getHeight());	
		if (!isDoubleBuffered()) setDoubleBuffered(true);
		
		for (int i = 0; i < shapes.size(); i++) {
			if(((Shape)shapes.get(i)).type()==1) { // Lineas
				g.setColor(((Line)shapes.get(i)).color);
				//System.out.print("Color: "+g.getColor()+"~");
				g.drawLine(((Line)shapes.get(i)).x1, ((Line)shapes.get(i)).y1, ((Line)shapes.get(i)).x2, ((Line)shapes.get(i)).y2);	
			} else { // Poligonos
				g.setColor(((MapPolygon)shapes.get(i)).getFillColor());
				g.fillPolygon((MapPolygon)shapes.get(i));			
			}
		}		
	}
	
	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground","true");
		int typeView = 0; // 0-Full, 1-Solo Aristas Visibles, 2-Solo Todas las Aristas.
		boolean giraPVO = false, ligthSource = false;
		int velocidadPVO = 10;
		double dPX = 60.0, dPY = 34.641, dPZ = 40.0, dDist = 1500.0;
		double dLX = 0.0, dLY = 100.0, dLZ = 0.0;
		if (args.length!=0) {
			for (String str: args) {
				if (str.indexOf("-t")!=-1) {
					typeView = Integer.parseInt(str.substring(2));
					continue;
				}
				if (str.indexOf("-p")!=-1) {
					String[] strArray = str.substring(2).split("~");
					dPX = Double.parseDouble(strArray[0]);
					dPY = Double.parseDouble(strArray[1]);
					dPZ = Double.parseDouble(strArray[2]);
					dDist = Double.parseDouble(strArray[3]);
					System.out.println(""+dPX+"|"+dPY+"|"+dPZ+"|"+dDist);		
					continue;
				}
				if (str.indexOf("-l")!=-1) {
					ligthSource = true;
					String[] strArray = str.substring(2).split("~");
					if (strArray.length == 3) {
						dLX = Double.parseDouble(strArray[0]);
						dLY = Double.parseDouble(strArray[1]);
						dLZ = Double.parseDouble(strArray[2]);
					}
					System.out.println(""+dLX+"|"+dLY+"|"+dLZ);		
					continue;
				}				
				System.out.println("Uso: java -cp <classpath> GraficaElipsoide [-t{0|1|2]}] [-pPosX~PosY~PosZ~DistPlanoProy] [-l[PosX~PosY~PosZ]]");
				System.out.println("Donde:");
				System.out.println("  -t : 0.Caras a Color, 1.Solo aristas visibles, 2.Transparente (todas las aristas visibles)");
				System.out.println("  -p : (PosX,PosY,PosZ)-Punto de vista del Observador, DistPlanoProy: Distancia al plano de Proyección");
				System.out.println("  -l : (PosX,PosY,PosZ)-Punto de fuente de luz...");
				return;
			}			
		}		
		JFrame testFrame = new JFrame();
		testFrame.setTitle("Elipsoide rotando proyectada..."+(typeView==0?"con caras pintadas":(typeView==1?"Solo aristas de caras visibles":"Solo esqueleto")));
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		PointR3 PVO = new PointR3(dPX,dPY,dPZ);
		GraficaElipsoide comp = new GraficaElipsoide(PVO, dDist); //testFrame.add(comp);
		PointR3 PVL = new PointR3(dLX,dLY,dLZ); // Punto donde esta la fuente de luz...
		System.out.println("PO X: "+comp.POX1+", PO Y: "+comp.POX2+", PO Z: "+comp.POX3);
		comp.setOrigen(683, 384);
		comp.setPreferredSize(new Dimension(comp.origenX*2, comp.origenY*2));
		testFrame.getContentPane().add(comp, BorderLayout.CENTER);
		testFrame.pack();
		testFrame.setVisible(true);			
	    // Ejes de coordenadas
		AristR3 AX1 = new AristR3(0.0,0.0,0.0,25.0,0.0,0.0);
		AristR3 AX2 = new AristR3(0.0,0.0,0.0,0.0,27.0,0.0);
		AristR3 AX3 = new AristR3(0.0,0.0,0.0,0.0,0.0,20.0);
		comp.addShape(AX1, Color.RED);
		comp.addShape(AX2, Color.GREEN);
		comp.addShape(AX3, Color.BLUE);

		int XO = (int)comp.projectedPoint(AX1.getIPoint()).getX(), YO = (int)comp.projectedPoint(AX1.getIPoint()).getY();
		//PointR3 PVO = new PointR3(comp.POX1, comp.POX2, comp.POX3);

		// Define ELIPSOIDE *************************************
		Elipsoide myElipsoide = new Elipsoide(new PointR3(0.0,0.0,0.0), 4.0, 8.0, 12.0, 14, 28, null /*Color.LIGHT_GRAY*/);
		int nShapes;
		
		if (typeView == 0) comp.setDrawColor(Color.BLACK);
	
		double VC[][], rL[] = null;
		boolean bSHX1, bSHX2, bSHX3;
		for (int i=0; i < 14400; i++) {
			nShapes = 0; // Cantidad de formas del dodecaedro dibujadas.
			bSHX1 = bSHX2 = bSHX3 = true; // Repinta el eje (salvo que haya cortado una cara de la figura, o sea tapada por esta).
			// Fragmentos de ejes de coordenadas que cortan cara/s a presentar al final, si fuera necesario, para evitar pisarlo por la presentacion de otra cara.
			LinkedList<Shape> X1_lineaDelayed = new LinkedList<Shape>(), X2_lineaDelayed = new LinkedList<Shape>(), X3_lineaDelayed = new LinkedList<Shape>();
			
			Triangulo currTriang;
			System.out.println("myElipsoide.size()="+myElipsoide.size());
			for (int j=0; j < myElipsoide.size(); j++) {
				currTriang = myElipsoide.getElem(j);	
				if (currTriang.esVisible(PVO) || typeView == 2) {
					if (typeView == 0) {
						double NS = (new matrix()).prod_escalar(currTriang.getPoint(1).getArr(), currTriang.getNormal().getArr());
						double DSX1 = (new matrix()).prod_escalar(new double [][]{{1.0},{0.0},{0.0}}, currTriang.getNormal().getArr());
						double DSX2 = (new matrix()).prod_escalar(new double [][]{{0.0},{1.0},{0.0}}, currTriang.getNormal().getArr());
						double DSX3 = (new matrix()).prod_escalar(new double [][]{{0.0},{0.0},{1.0}}, currTriang.getNormal().getArr());
						PointR3 pCX1 = new PointR3(NS/DSX1, 0.0, 0.0);
						Point pProyCX1 = comp.projectedPoint( pCX1 );
						PointR3 pCX2 = new PointR3(0.0, NS/DSX2, 0.0);
						Point pProyCX2 = comp.projectedPoint( pCX2 );
						PointR3 pCX3 = new PointR3(0.0, 0.0, NS/DSX3);
						Point pProyCX3 = comp.projectedPoint( pCX3 );						
						comp.addShape(currTriang, currTriang.getFillColor());
						nShapes+=1;	
						Polygon POL = (Polygon)comp.getLastShape();
						if (((int)DSX1) != 0 && POL.getBounds2D().contains(pProyCX1.getX(),pProyCX1.getY()) && pCX1.getX() > 0) { // Eje X cortando Triangulo j
							System.out.println("(S"+j+")### - pProyCX1.getX(): "+pProyCX1.getX()+", pProyCX1.getY(): "+pProyCX1.getY()+"------pCX1.getX(): "+pCX1.getX());
							// Muestra punto de corte de la superficie con el eje X con una pequeña cruz.
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()-2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()+2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX1 > 0) { // Guarda fragmento de recta desde intersección de cara con eje X en adelante, para mostrar despues.
						        X1_lineaDelayed.add(new Line((int)pProyCX1.getX(), (int)pProyCX1.getY(), (int)comp.projectedPoint(AX1.getEPoint()).getX(), (int)comp.projectedPoint(AX1.getEPoint()).getY(), Color.RED));	
							} else { // Guarda fragmento de recta desde origen a intersección de cara con eje X, para mostrar despues.
						        X1_lineaDelayed.add(new Line(XO, YO, (int)pProyCX1.getX(), (int)pProyCX1.getY(), Color.RED));	
							} 
							bSHX1 = false;
							System.out.println("(S"+j+")Pone bSHX1 a false 1");
						}
						if (((int)DSX2) != 0 && POL.getBounds2D().contains(pProyCX2.getX(),pProyCX2.getY()) && pCX2.getY() > 0) { // Eje Y cortando Triangulo j
							System.out.println("(S"+j+")### - pProyCX2.getX(): "+pProyCX2.getX()+", pProyCX2.getY(): "+pProyCX2.getY()+"------pCX2.getY(): "+pCX2.getY());
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()-2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()+2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX2 > 0) {
						        X2_lineaDelayed.add(new Line((int)pProyCX2.getX(), (int)pProyCX2.getY(), (int)comp.projectedPoint(AX2.getEPoint()).getX(), (int)comp.projectedPoint(AX2.getEPoint()).getY(), Color.GREEN));
								
							} else {
						        X2_lineaDelayed.add(new Line(XO, YO, (int)pProyCX2.getX(), (int)pProyCX2.getY(), Color.GREEN));	
							} 
							bSHX2 = false;
							System.out.println("(S"+j+")Pone bSHX2 a false 1");
						}
						if (((int)DSX3) != 0 && POL.getBounds2D().contains(pProyCX3.getX(),pProyCX3.getY()) && pCX3.getZ() > 0) { // Eje Z cortando Triangulo j
							System.out.println("(S"+j+")### - pProyCX3.getX(): "+pProyCX3.getX()+", pProyCX3.getY(): "+pProyCX3.getY()+"------pCX3.getZ(): "+pCX3.getZ());
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()-2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()+2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX3 > 0) {
						        X3_lineaDelayed.add(new Line((int)pProyCX3.getX(), (int)pProyCX3.getY(), (int)comp.projectedPoint(AX3.getEPoint()).getX(), (int)comp.projectedPoint(AX3.getEPoint()).getY(), Color.BLUE));
								
							} else {
						        X3_lineaDelayed.add(new Line(XO, YO, (int)pProyCX3.getX(), (int)pProyCX3.getY(), Color.BLUE));	
							} 
							bSHX3 = false;
							System.out.println("(S"+j+")Pone bSHX3 a false 1");
						}						
					}	
					nShapes+=comp.addShapes(currTriang);
				}
				/*comp.repaint();
				try{
					Thread.sleep(100);
				}catch(InterruptedException ex){
					Thread.currentThread().interrupt();
				}*/			
			}
			
		    if (bSHX1) {
				comp.addShape(AX1, Color.RED);
				nShapes+=1;
			} else {
				nShapes+=comp.addShapes(X1_lineaDelayed);
			}
			if (bSHX2) {
				comp.addShape(AX2, Color.GREEN);
				nShapes+=1;
			} else {
				nShapes+=comp.addShapes(X2_lineaDelayed);
			}
			if (bSHX3) {
				comp.addShape(AX3, Color.BLUE);
				nShapes+=1;
			} else {
				nShapes+=comp.addShapes(X3_lineaDelayed);
			}
			comp.repaint();
			try{
				Thread.sleep(50);
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}

			for (int k=0; k < myElipsoide.size(); k++) {
				currTriang = myElipsoide.getElem(k);
				if (i < 360) {
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,1), currTriang.getHArr());
				} else if (i < 720) {
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,1), currTriang.getHArr());
				} else if (i < 1080) {
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,1), currTriang.getHArr());
				} else { // Gira "descontrolado"...
						int sentido = 1; //(Math.random()>0.5?1:-1);
						VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,sentido*(int)Math.abs(4*Math.sin(comp.grado*i))), currTriang.getHArr()); 
						VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,sentido*(int)Math.abs(3*Math.cos(2*comp.grado*i))), VC); 
						VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,sentido*(int)Math.abs(2*Math.sin(3*comp.grado*i))), VC); 
				}			
				myElipsoide.setElem(k, new Triangulo(VC,currTriang.getFillColor()));
			}			
			comp.clearLastNShapes(nShapes);
		}
		
		testFrame.setVisible(false);
		testFrame.dispose();
		return;
	}
}