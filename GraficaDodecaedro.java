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

public class GraficaDodecaedro extends Perspectiva {
	private Color drawColor;

	public GraficaDodecaedro () {
		super();
	}
	
	public GraficaDodecaedro (PointR3 pobs, double DP_) {
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
		
		public int getXCoordPoint(int nPoint) {
			if (nPoint < 1 || nPoint >5) return -10000000; // Solo un control, sería mejor lanzar una excepción
			return xpoints[nPoint-1];
		}
		
		public int getYCoordPoint(int nPoint) {
			if (nPoint < 1 || nPoint >5) return -10000000; // Solo un control, sería mejor lanzar una excepción
			return ypoints[nPoint-1];
		}		
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

	// *** Crea el perimetro de un pentagono, generando 5 objetos Line que se agregan a la Lista encadenada de Formas con un color determinado.
	public int addShapes(Pentagono p, Color color) {
		addShape(p.getArist(1), color);
		addShape(p.getArist(2), color);
		addShape(p.getArist(3), color);
		addShape(p.getArist(4), color);
		addShape(p.getArist(5), color);
		return 5;
	}

	// *** Crea el perimetro de un pentagono con el color por defecto o blanco, invocando un metodo mas especializado que carga las lineas del mismo.
	public int addShapes(Pentagono p) {
		return addShapes(p, (this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}

	// *** Agrega formas de otra lista encadenada a la lista encadenada principal, retornando la cantidad de formas agregadas.
	public int addShapes(LinkedList<Shape> sourceShapes) {
		int srcLen = sourceShapes.size();
		for (int i = 0; i < srcLen; i++) {
			shapes.add((Shape)sourceShapes.get(i));
		}
		return srcLen;
	}
	
	// *** Agrega un poligono "Pentagonal" a la lista encadenada de formas, en base a la coordenada de los puntos.
	public void addShape(int x1, int x2, int x3, int x4, int x5, int y1, int y2, int y3, int y4, int y5, Color color) {
		shapes.add(new MapPolygon(new int[]{x1,x2,x3, x4, x5}, new int[]{y1,y2,y3, y4, y5}, 5, color));        
	}	
	// *** En base a un objeto "Pentagono" en el espacio (R3), proyecta sus puntos en R2 mediante la perspectiva en uso e invoca un metodo especializado
	// *** para agregar un objeto "MapPoligon" con las coord{1enadas proyectadas (en R2), a la lista encadenada de formas.	
	public void addShape(Pentagono p, Color color) {
		addShape(projectedPoint(p.getPoint(1)).x, projectedPoint(p.getPoint(2)).x, projectedPoint(p.getPoint(3)).x, projectedPoint(p.getPoint(4)).x, projectedPoint(p.getPoint(5)).x, projectedPoint(p.getPoint(1)).y, projectedPoint(p.getPoint(2)).y, projectedPoint(p.getPoint(3)).y, projectedPoint(p.getPoint(4)).y, projectedPoint(p.getPoint(5)).y, color);
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

	// Remueve las últimas N formas de la lista encadenada, retornando un array con las N formas eliminadas.
	public Shape [] clearLastNShapes(int n) {
		Shape lastNShapes [] = new Shape [n];
		for (int i=0; i < n; i++) {
			lastNShapes[i]=clearLastShape();
		}
		return lastNShapes;
	}

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
				if (str.indexOf("-g")!=-1) {
					giraPVO = true;
					if (str.length()>2) {
						velocidadPVO = Integer.parseInt(str.substring(2));
					}
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
				System.out.println("Uso: java -cp <classpath> GraficaDodecaedro [-t{0|1|2]}] [-pPosX~PosY~PosZ~DistPlanoProy] [-g[Vel]] [-l[PosX~PosY~PosZ]]");
				System.out.println("Donde:");
				System.out.println("  -t : 0.Caras a Color, 1.Solo aristas visibles, 2.Transparente (todas las aristas visibles)");
				System.out.println("  -p : (PosX,PosY,PosZ)-Punto de vista del Observador, DistPlanoProy: Distancia al plano de Proyección");
				System.out.println("  -g : Cambio del punto de vista del observador girando alrrededor del eje Z. Vel: Velocidad (1.Mas Veloz, a mayor valor menor velocidad)");
				System.out.println("  -l : (PosX,PosY,PosZ)-Punto de fuente de luz...");
				return;
			}			
		}		
		JFrame testFrame = new JFrame();
		testFrame.setTitle("Dodecaedro rotando proyectado..."+(typeView==0?"con caras pintadas":(typeView==1?"Solo aristas de caras visibles":"Solo esqueleto")));
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		PointR3 PVO = new PointR3(dPX,dPY,dPZ);
		/*final*/ GraficaDodecaedro comp = new GraficaDodecaedro(PVO, dDist); //testFrame.add(comp);
		//PointR3 PVL = new PointR3((new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,45), PVO.getHArr()));
		PointR3 PVL = new PointR3(dLX,dLY,dLZ); // Punto donde esta la fuente de luz "precaria"...
		//final GraficaDodecaedro comp = new GraficaDodecaedro(new PointR3(30.0,17.320508075688767,20.000000000000004), 300.0);
		System.out.println("PO X: "+comp.POX1+", PO Y: "+comp.POX2+", PO Z: "+comp.POX3);
		//comp.setDrawColor(Color.RED);
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
		Dodecaedro myDodecaedro = new Dodecaedro(new PointR3(0,-1.0/Forma.PA,-Forma.PA), new PointR3(0,1.0/Forma.PA,-Forma.PA), new PointR3(1,1,-1), new PointR3(Forma.PA,0,-1.0/Forma.PA), 
								new PointR3(1,-1,-1), new PointR3(-1,-1,-1), new PointR3(-Forma.PA,0,-1.0/Forma.PA), new PointR3(-1,1,-1),
								new PointR3(-1.0/Forma.PA,Forma.PA,0), new PointR3(1.0/Forma.PA,Forma.PA,0), new PointR3(1,1,1), new PointR3(Forma.PA,0,1.0/Forma.PA),
								new PointR3(1,-1,1), new PointR3(1.0/Forma.PA,-Forma.PA,0), new PointR3(-1.0/Forma.PA,-Forma.PA,0), new PointR3(-1,-1,1),
								new PointR3(0,-1.0/Forma.PA,Forma.PA), new PointR3(0,1.0/Forma.PA,Forma.PA), new PointR3(-1,1,1), new PointR3(-Forma.PA,0,1.0/Forma.PA));

		int nShapes;
		double trasladoDodecaedro[][]={{1,0,0,0},{0,1,0,3.5*(1.0/Forma.PA)},{0,0,1,3.5*Forma.PA},{0,0,0,1}};
		// Esqueleto de Dodecaedro ubicado y creciendo a la dimensión a usar
		Dodecaedro dCopy = myDodecaedro;
		for (double id = 4.0; id<=250.0; id+=2.0) {
			nShapes = 0;
			dCopy = new Dodecaedro((new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,32),(new matrix()).producto(trasladoDodecaedro,new Dodecaedro(myDodecaedro.getHArr(), 1.0+(id/100.0)).getHArr())));
			for (int jg=1; jg<13; jg++) {
				nShapes+=comp.addShapes(dCopy.getFace(jg));
			}
			comp.repaint();
			try{
				Thread.sleep(75);
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			comp.clearLastNShapes(nShapes);
		}
		myDodecaedro = new Dodecaedro(dCopy.getHArr());
		//myDodecaedro.escalado(3.5);
		//myDodecaedro = new Dodecaedro((new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,32),(new matrix()).producto(trasladoDodecaedro,myDodecaedro.getHArr())));
		if (typeView == 0) comp.setDrawColor(Color.BLACK);
	
		double VC[][], rL[] = null;
		boolean bSHX1, bSHX2, bSHX3;
		//Color face_color[] = /*new Color[]*/ {Color.PINK, Color.GREEN, Color.BLUE, Color.MAGENTA , Color.YELLOW, Color.CYAN, Color.LIGHT_GRAY, Color.ORANGE, Color.WHITE, Color.RED, Color.DARK_GRAY, Color.GRAY}; // El "new Color[]" no sería necesario.
		Color face_color[] = /*new Color[]*/ {Color.PINK, new Color(51,153,255) /*Dodger Blue*/, Color.GREEN, new Color(245,102,245) /*Light MAGENTA*/ , Color.YELLOW, new Color(128,0,128) /* Fucsia */, new Color(78,59,49) /*Marron Tierra*/, new Color(255,140,0) /*Dark Orange*/, Color.WHITE, new Color(220,20,60) /*Rojo Crimson*/, Color.CYAN, new Color(84,101,0) /*Verde Oliva Antes Color.GRAY*/};
		for (int i=0; i < 14400; i++) {
			if (giraPVO && (i+1)%velocidadPVO == 0) { // Punto de vista del observador variable.
				PVO = new PointR3((new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,1), PVO.getHArr()));
				comp.update(PVO, dDist);
				comp.clearFirstNShapes(3); // Borra los ejes para redibujarlos, al variar el punto de vista del observador.
				comp.addShape(AX1, Color.RED);
				comp.addShape(AX2, Color.GREEN);
				comp.addShape(AX3, Color.BLUE);
				XO = (int)comp.projectedPoint(AX1.getIPoint()).getX();
				YO = (int)comp.projectedPoint(AX1.getIPoint()).getY();				
			}
			nShapes = 0; // Cantidad de formas del dodecaedro dibujadas.
			bSHX1 = bSHX2 = bSHX3 = true; // Repinta el eje (salvo que haya cortado una cara de la figura, o sea tapada por esta).
			// Para guardar Fragmentos de ejes de coordenadas que cortan cara/s, a presentar al final, si fuera necesario, para evitar pisarlo por la presentacion de otra cara.
			LinkedList<Shape> X1_lineaDelayed = new LinkedList<Shape>(), X2_lineaDelayed = new LinkedList<Shape>(), X3_lineaDelayed = new LinkedList<Shape>();
			
			/* Producto Mixto, si el resultado es positivo es cara visible, si es menor o igual que cero es cara no visible.
			** Recordar que el producto mixto conjuga un producto vectorial de la 2da y 3er fila y luego el vector resultante (normal a  
			** la cara) es multiplicado escalarmente por la primer fila; en definitiva es lo mismo que calcular el determinante de la matriz.*/	
			double rS[] = new double[] {	(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(1).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(1).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(2).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(2).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(3).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(3).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(4).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(4).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(5).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(5).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(6).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(6).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(7).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(7).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(8).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(8).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(9).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(9).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(10).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(10).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(11).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(11).getArr()),
											(new matrix()).prod_escalar(PointR3.getPointDiff(PVO, myDodecaedro.getFace(12).getPoint(1)).getArr(), myDodecaedro.getFaceNormal(12).getArr())
											};
			//face_color = new Color[] {Color.PINK, new Color(51+(i%200),153,255) /*Dodger Blue*/, Color.GREEN, new Color(245,102+(i%153),245) /*Light MAGENTA*/ , Color.YELLOW, new Color(128,0+(i%255),128) /* Fucsia */, new Color(78,59,49+(i%200)) /*Marron Tierra*/, new Color(255,140,0+(i%255)) /*Dark Orange*/, Color.WHITE, new Color(220,20+(i%235),60) /*Rojo Crimson*/, Color.CYAN, new Color(84,101,0+(i%255)) /*Verde Oliva Antes Color.GRAY*/};
			//face_color = new Color[] {Color.PINK, new Color(51,153,255,i%255) /*Dodger Blue*/, Color.GREEN, new Color(245,102,245,i%255) /*Light MAGENTA*/ , Color.YELLOW, new Color(128,0,128,i%255) /* Fucsia */, new Color(78,59,49,i%255) /*Marron Tierra*/, new Color(255,140,0,i%255) /*Dark Orange*/, Color.WHITE, new Color(220,20,60,i%255) /*Rojo Crimson*/, Color.CYAN, new Color(84,101,0,i%255) /*Verde Oliva Antes Color.GRAY*/};
			if (ligthSource) {
				// En un intento por simular una fuente de luz "precaria" alumbrando la figura en movimiento...
				rL = new double[] {	(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(1).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(1).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(2).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(2).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(3).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(3).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(4).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(4).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(5).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(5).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(6).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(6).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(7).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(7).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(8).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(8).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(9).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(9).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(10).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(10).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(11).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(11).getArr()),
									(new matrix()).prod_escalar(new AristR3(myDodecaedro.getFace(12).getPoint(1), PVL).getVersor().getArr(), myDodecaedro.getFaceNormalVersor(12).getArr())											
									};
			}

			for (int j=1; j<13; j++) {
			//for (int j=12; j>0; j--) {				
				if (rS[j-1] >= 0 || typeView == 2) {
					if (typeView == 0) {
						double NS = (new matrix()).prod_escalar(myDodecaedro.getFace(j).getPoint(1).getArr(), myDodecaedro.getFaceNormal(j).getArr());
						double DSX1 = (new matrix()).prod_escalar(new double [][]{{1.0},{0.0},{0.0}}, myDodecaedro.getFaceNormal(j).getArr());
						double DSX2 = (new matrix()).prod_escalar(new double [][]{{0.0},{1.0},{0.0}}, myDodecaedro.getFaceNormal(j).getArr());
						double DSX3 = (new matrix()).prod_escalar(new double [][]{{0.0},{0.0},{1.0}}, myDodecaedro.getFaceNormal(j).getArr());
						PointR3 pCX1 = new PointR3(NS/DSX1, 0.0, 0.0);
						Point pProyCX1 = comp.projectedPoint( pCX1 );
						PointR3 pCX2 = new PointR3(0.0, NS/DSX2, 0.0);
						Point pProyCX2 = comp.projectedPoint( pCX2 );
						PointR3 pCX3 = new PointR3(0.0, 0.0, NS/DSX3);
						Point pProyCX3 = comp.projectedPoint( pCX3 );
						if (ligthSource) {
							System.out.println("Math.acos(rL["+(j-1)+"])="+Math.acos(rL[j-1])+"......Math.PI/2="+Math.PI/2);
							comp.addShape(myDodecaedro.getFace(j), (rL[j-1]>0&&Math.acos(rL[j-1])<Math.PI/2?face_color[j-1].brighter():(rL[j-1]<0&&Math.acos(rL[j-1])>2*Math.PI/3?face_color[j-1].darker():face_color[j-1])));
						} else {
							comp.addShape(myDodecaedro.getFace(j), face_color[j-1]);
						}
						nShapes+=1;	
						Polygon POL = (Polygon)comp.getLastShape();
						if (((int)DSX1) != 0 && POL.contains(pProyCX1.getX(),pProyCX1.getY()) && pCX1.getX() > 0) { // Eje X cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX1.getX(): "+pProyCX1.getX()+", pProyCX1.getY(): "+pProyCX1.getY()+"------pCX1.getX(): "+pCX1.getX());
							// Muestra punto de corte de la superficie con el eje X con una pequeña cruz.
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()-2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()+2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX1 > 0) { // Guarda fragmento de recta desde intersección de cara con eje X en adelante, para mostrar despues.
								//comp.addShape((int)pProyCX1.getX(), (int)pProyCX1.getY(), (int)comp.projectedPoint(AX1.getEPoint()).getX(), (int)comp.projectedPoint(AX1.getEPoint()).getY(), Color.RED);
						        X1_lineaDelayed.add(new Line((int)pProyCX1.getX(), (int)pProyCX1.getY(), (int)comp.projectedPoint(AX1.getEPoint()).getX(), (int)comp.projectedPoint(AX1.getEPoint()).getY(), Color.RED));	
							} else { // Guarda fragmento de recta desde origen a intersección de cara con eje X, para mostrar despues.
								//comp.addShape(XO, YO, (int)pProyCX1.getX(), (int)pProyCX1.getY(), Color.RED);
						        X1_lineaDelayed.add(new Line(XO, YO, (int)pProyCX1.getX(), (int)pProyCX1.getY(), Color.RED));	
							} 
							bSHX1 = false;
							System.out.println("(S"+j+")Pone bSHX1 a false 1");
						} else {
							// Punto sobre el eje X para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje X al punto del observador, entonces la cara esta tapando el eje X (X1).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada X como pto. de la recta.
							///double x0 = (((int)DSX1) == 0?PointR3.getPointAvg(myDodecaedro.getFace(j).getPoint(1),myDodecaedro.getFace(j).getPoint(3)).getX():0);
							double testCoord[] = {PointR3.getPointAvg(myDodecaedro.getFace(j).getPoint(1),myDodecaedro.getFace(j).getPoint(4)).getX(), 0.0}; // Reempl. 3 x 4
							for (int k=0; k < testCoord.length && bSHX1; k++) {
								double x0 =  testCoord[k];
								PointR3 X0 = new PointR3(x0,0,0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myDodecaedro.getFace(j).getPoint(1), X0).getArr(), myDodecaedro.getFaceNormal(j).getArr());
								AristR3 PVO_X0 = new AristR3(X0,PVO);
								double den = (new matrix()).prod_escalar(PVO_X0.getVersor().getArr(), myDodecaedro.getFaceNormal(j).getArr());
								double X1CParam = num / den; 
								PointR3 X1C = PointR3.getPointSum(X0, PVO_X0.getVersor().escalado(X1CParam)); // Punto de corte en el espacio del plano de la cara con la recta PVO_X0
								AristR3 PVO_X1C = new AristR3(X1C,PVO);
								System.out.println("(S"+j+")### Dist. x0 a PVO: "+PVO_X0.getLength()+"Dist. intersecc. S con recta x0-PVO: "+PVO_X1C.getLength());
								if (PVO_X0.getLength() > PVO_X1C.getLength() && Math.abs(PVO_X0.getLength()-PVO_X1C.getLength()) > 0.001) { // Posible ocultamiento del eje X1 con cara
									Point X1C_proy = comp.projectedPoint(X1C);
									if (POL.getBounds2D().contains(X1C_proy.getX(),X1C_proy.getY())) {
										System.out.println("(S"+j+")***** - Xproy: "+X1C_proy.getX()+", Yproy: "+X1C_proy.getY()+", Pone bSHX1 a false 2");
										bSHX1 = false;
									} else {
										System.out.println("El punto de corte no caeria dentro de (S"+j+"), eje X posiblemente no sea ocultado");
										System.out.println("(S"+j+")-Punto que caeria fuera del Poligono: ("+(int)X1C_proy.getX()+","+(int)X1C_proy.getY()+")");
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+") - P5: ("+((MapPolygon)POL).getXCoordPoint(5)+","+((MapPolygon)POL).getYCoordPoint(5)+")");
										//comp.addShape((int)X1C_proy.getX()-2, (int)X1C_proy.getY()-2, (int)X1C_proy.getX()+2, (int)X1C_proy.getY()+2, Color.RED);
										//comp.addShape((int)X1C_proy.getX()-2, (int)X1C_proy.getY()+2, (int)X1C_proy.getX()+2, (int)X1C_proy.getY()-2, Color.WHITE);
										//nShapes+=2;
									}
								}								
							} // End FOR	
						}
						if (((int)DSX2) != 0 && POL.contains(pProyCX2.getX(),pProyCX2.getY()) && pCX2.getY() > 0) { // Eje Y cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX2.getX(): "+pProyCX2.getX()+", pProyCX2.getY(): "+pProyCX2.getY()+"------pCX2.getY(): "+pCX2.getY());
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()-2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()+2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX2 > 0) {
								//comp.addShape((int)pProyCX2.getX(), (int)pProyCX2.getY(), (int)comp.projectedPoint(AX2.getEPoint()).getX(), (int)comp.projectedPoint(AX2.getEPoint()).getY(), Color.GREEN);
						        X2_lineaDelayed.add(new Line((int)pProyCX2.getX(), (int)pProyCX2.getY(), (int)comp.projectedPoint(AX2.getEPoint()).getX(), (int)comp.projectedPoint(AX2.getEPoint()).getY(), Color.GREEN));
								
							} else {
								//comp.addShape(XO, YO, (int)pProyCX2.getX(), (int)pProyCX2.getY(), Color.GREEN);
						        X2_lineaDelayed.add(new Line(XO, YO, (int)pProyCX2.getX(), (int)pProyCX2.getY(), Color.GREEN));	
							} 
							bSHX2 = false;
							System.out.println("(S"+j+")Pone bSHX2 a false 1");
						} else {
							// Punto sobre el eje Y para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje Y al punto del observador, entonces la cara esta tapando el eje Y (X2).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada Y como pto. de la recta.
							//double y0 = (((int)DSX2) == 0?PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getY():0);
							///double y0 = PointR3.getPointAvg(myDodecaedro.getFace(j).getPoint(1),myDodecaedro.getFace(j).getPoint(3)).getY();
							double testCoord[] = {PointR3.getPointAvg(myDodecaedro.getFace(j).getPoint(1),myDodecaedro.getFace(j).getPoint(3)).getY(),0.0};
							for (int k=0; k < testCoord.length && bSHX2; k++) {
								double y0 =  testCoord[k];
								PointR3 Y0 = new PointR3(0,y0,0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myDodecaedro.getFace(j).getPoint(1), Y0).getArr(), myDodecaedro.getFaceNormal(j).getArr());
								AristR3 PVO_Y0 = new AristR3(Y0,PVO);
								double den = (new matrix()).prod_escalar(PVO_Y0.getVersor().getArr(), myDodecaedro.getFaceNormal(j).getArr());
								double X2CParam = num / den; 
								PointR3 X2C = PointR3.getPointSum(Y0, PVO_Y0.getVersor().escalado(X2CParam)); // Punto de corte en el espacio del plano de la cara con la recta PVO_Y0
								AristR3 PVO_X2C = new AristR3(X2C,PVO);
								System.out.println("(S"+j+")### Dist. y0 a PVO: "+PVO_Y0.getLength()+"Dist. intersecc. S con recta y0-PVO: "+PVO_X2C.getLength());
								if (PVO_Y0.getLength() > PVO_X2C.getLength() && Math.abs(PVO_Y0.getLength()-PVO_X2C.getLength()) > 0.001) { // Posible ocultamiento del eje X2 con cara
									Point X2C_proy = comp.projectedPoint(X2C);
									if (POL.getBounds2D().contains(X2C_proy.getX(),X2C_proy.getY())) {
										System.out.println("(S"+j+")***** - Xproy: "+X2C_proy.getX()+", Yproy: "+X2C_proy.getY()+", Pone bSHX2 a false 2");
										bSHX2 = false;
									} else {
										System.out.println("El punto de corte no caeria dentro de (S"+j+"), eje Y posiblemente no sea ocultado");
										System.out.println("(S"+j+")-Punto que caeria fuera del Poligono: ("+(int)X2C_proy.getX()+","+(int)X2C_proy.getY()+")");
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+") - P5: ("+((MapPolygon)POL).getXCoordPoint(5)+","+((MapPolygon)POL).getYCoordPoint(5)+")");
										//comp.addShape((int)X2C_proy.getX()-4, (int)X2C_proy.getY()-4, (int)X2C_proy.getX()+4, (int)X2C_proy.getY()+4, Color.GREEN);
										//comp.addShape((int)X2C_proy.getX()-4, (int)X2C_proy.getY()+4, (int)X2C_proy.getX()+4, (int)X2C_proy.getY()-4, Color.WHITE);
										//nShapes+=2;
									}
								}
							} // End FOR
						}
						if (((int)DSX3) != 0 && POL.contains(pProyCX3.getX(),pProyCX3.getY()) && pCX3.getZ() > 0) { // Eje Z cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX3.getX(): "+pProyCX3.getX()+", pProyCX3.getY(): "+pProyCX3.getY()+"------pCX3.getZ(): "+pCX3.getZ());
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()-2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()+2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()-2, Color.BLACK);
							nShapes+=2;
							if (DSX3 > 0) {
								//comp.addShape((int)pProyCX3.getX(), (int)pProyCX3.getY(), (int)comp.projectedPoint(AX3.getEPoint()).getX(), (int)comp.projectedPoint(AX3.getEPoint()).getY(), Color.BLUE);
						        X3_lineaDelayed.add(new Line((int)pProyCX3.getX(), (int)pProyCX3.getY(), (int)comp.projectedPoint(AX3.getEPoint()).getX(), (int)comp.projectedPoint(AX3.getEPoint()).getY(), Color.BLUE));
								
							} else {
								//comp.addShape(XO, YO, (int)pProyCX3.getX(), (int)pProyCX3.getY(), Color.BLUE);
						        X3_lineaDelayed.add(new Line(XO, YO, (int)pProyCX3.getX(), (int)pProyCX3.getY(), Color.BLUE));	
							} 
							bSHX3 = false;
							System.out.println("(S"+j+")Pone bSHX3 a false 1");
						} else {
							// Punto sobre el eje Z para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje Z al punto del observador, entonces la cara esta tapando el eje Z (X3).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada Z como pto. de la recta.
							//double z0 = (((int)DSX3) == 0?PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getZ():0);
							double testCoord[] = {PointR3.getPointAvg(myDodecaedro.getFace(j).getPoint(1),myDodecaedro.getFace(j).getPoint(3)).getZ(), 0.0};
							for (int k=0; k < testCoord.length && bSHX3; k++) {
								double z0 =  testCoord[k];
								PointR3 Z0 = new PointR3(0,0,z0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myDodecaedro.getFace(j).getPoint(1), Z0).getArr(), myDodecaedro.getFaceNormal(j).getArr());
								AristR3 PVO_Z0 = new AristR3(Z0,PVO);
								double den = (new matrix()).prod_escalar(PVO_Z0.getVersor().getArr(), myDodecaedro.getFaceNormal(j).getArr());
								double X3CParam = num / den; 
								PointR3 X3C = PointR3.getPointSum(Z0, PVO_Z0.getVersor().escalado(X3CParam)); // Punto de corte en el espacio del plano de la cara con la recta PVO_Z0
								AristR3 PVO_X3C = new AristR3(X3C,PVO);
								System.out.println("(S"+j+")### Dist. z0 a PVO: "+PVO_Z0.getLength()+"Dist. intersecc. S con recta z0-PVO: "+PVO_X3C.getLength());
								if (PVO_Z0.getLength() > PVO_X3C.getLength() && Math.abs(PVO_Z0.getLength()-PVO_X3C.getLength()) > 0.001) { // Posible ocultamiento del eje X3 con cara
									Point X3C_proy = comp.projectedPoint(X3C);
									if (POL.getBounds2D().contains(X3C_proy.getX(),X3C_proy.getY())) { //POL.getBounds2D()
										System.out.println("(S"+j+")***** - Xproy: "+X3C_proy.getX()+", Yproy: "+X3C_proy.getY()+", Pone bSHX3 a false 2");
										bSHX3 = false;
									} else {
										System.out.println("El punto de corte no caeria dentro de (S"+j+"), eje Z posiblemente no sea ocultado");
										System.out.println("(S"+j+")-Punto que caeria fuera del Poligono: ("+(int)X3C_proy.getX()+","+(int)X3C_proy.getY()+")");
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+") - P5: ("+((MapPolygon)POL).getXCoordPoint(5)+","+((MapPolygon)POL).getYCoordPoint(5)+")");
										//comp.addShape((int)X3C_proy.getX()-2, (int)X3C_proy.getY()-2, (int)X3C_proy.getX()+2, (int)X3C_proy.getY()+2, Color.BLUE);
										//comp.addShape((int)X3C_proy.getX()-2, (int)X3C_proy.getY()+2, (int)X3C_proy.getX()+2, (int)X3C_proy.getY()-2, Color.WHITE);
										//nShapes+=2;
									} 
								}								
							} // End FOR							
						}					
					}					
					nShapes+=comp.addShapes(myDodecaedro.getFace(j));			
				}				
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

			if (i < 360) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,1), myDodecaedro.getHArr()); // Gira el cubo un grado en torno del eje X (X1)
			} else if (i < 720) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,1), myDodecaedro.getHArr()); // Gira el cubo un grado en torno del eje Y (X2)
			} else if (i < 1080) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,1), myDodecaedro.getHArr()); // Gira el cubo un grado en torno del eje Z (X3)
			} else { // Gira "descontrolado"...
					int sentido = 1; //(Math.random()>0.5?1:-1);
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,sentido*(int)Math.abs(4*Math.sin(comp.grado*i))), myDodecaedro.getHArr()); 
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,sentido*(int)Math.abs(3*Math.cos(2*comp.grado*i))), VC); 
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,sentido*(int)Math.abs(2*Math.sin(3*comp.grado*i))), VC); 
			}			
			myDodecaedro = new Dodecaedro(VC);
			comp.clearLastNShapes(nShapes);
		}
		
		testFrame.setVisible(false); //Ya no me puedes ver
		testFrame.dispose(); //Destruye el objeto JFrame
		return;
	}
}
