import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JComponent;

public class GraficaCubo extends Perspectiva {
	private Color drawColor;

	public Color getDrawColor() {
		return drawColor;
	}

	public void setDrawColor(Color drawColor) {
		this.drawColor = drawColor;
	}

	private interface Shape {
		public int type();
	}

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
	
	private static class MapPolygon extends Polygon implements Shape {

		private Color fillColor;
		int[] xpoints, ypoints;

		public int type() {
			return 2;
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
			if (nPoint < 1 || nPoint >4) return -10000000;
			return xpoints[nPoint-1];
		}
		
		public int getYCoordPoint(int nPoint) {
			if (nPoint < 1 || nPoint >4) return -10000000;
			return ypoints[nPoint-1];
		}		
	}	

	private final LinkedList<Shape> shapes = new LinkedList<Shape>();

	public void addShape(int x1, int y1, int x2, int y2) {
		addShape(x1, y1, x2, y2, (this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}

	public void addShape(int x1, int y1, int x2, int y2, Color color) {
		shapes.add(new Line(x1,y1,x2,y2, color));        
	}
	
	// *** Crea el perimetro de un cuadrado, generando 4 objetos Line que se agregan a la Lista encadenada de Formas con un color determinado.
	public int addShapes(Rectangulo r, Color color) {
		addShape(r.getArist(1), color);
		addShape(r.getArist(2), color);
		addShape(r.getArist(3), color);
		addShape(r.getArist(4), color);
		return 4;
	}
	
	// *** Crea el perimetro de un cuadrado con el color por defecto o blanco, invocando un metodo mas especializado que carga las lineas del mismo.
	public int addShapes(Rectangulo r) {
		return addShapes(r, (this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}
	
	public void addShape(AristR3 ar, Color color) {
		//shapes.add(new Line(projectedPoint(ar.getIPoint()).x,projectedPoint(ar.getIPoint()).y,projectedPoint(ar.getEPoint()).x,projectedPoint(ar.getEPoint()).y, color));        
   		addShape(projectedPoint(ar.getIPoint()).x,projectedPoint(ar.getIPoint()).y,projectedPoint(ar.getEPoint()).x,projectedPoint(ar.getEPoint()).y, color);        

	}	

	public void addShape(AristR3 ar) {
		addShape(ar,(this.getDrawColor()!=null?this.getDrawColor():Color.white));
	}
	
	// *** Agrega un poligono "rectangular" a la lista encadenada de formas, en base a la coordenada de los puntos.
	public void addShape(int x1, int x2, int x3, int x4, int y1, int y2, int y3, int y4, Color color) {
		shapes.add(new MapPolygon(new int[]{x1,x2,x3, x4}, new int[]{y1,y2,y3, y4}, 4, color));        
	}

	// *** En base a un objeto "Rectangulo" en el espacio (R3), proyecta sus puntos en R2 mediante la perspectiva en uso e invoca un metodo especializado
	// *** para agregar un objeto "MapPoligon" con las coordenadas proyectadas (en R2), a la lista encadenada de formas.
	public void addShape(Rectangulo r, Color color) {
		addShape(projectedPoint(r.getPoint(1)).x, projectedPoint(r.getPoint(2)).x, projectedPoint(r.getPoint(3)).x, projectedPoint(r.getPoint(4)).x, projectedPoint(r.getPoint(1)).y, projectedPoint(r.getPoint(2)).y, projectedPoint(r.getPoint(3)).y, projectedPoint(r.getPoint(4)).y, color);
	}
	
	public Shape clearLastShape() {
		Shape lastShape = shapes.removeLast();
		return lastShape;
	}

	public Shape getLastShape() {
		Shape lastShape = shapes.peekLast();
		return lastShape;
	}

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
				g.drawLine(((Line)shapes.get(i)).x1, ((Line)shapes.get(i)).y1, ((Line)shapes.get(i)).x2, ((Line)shapes.get(i)).y2);	
			} else { // Poligonos
				g.setColor(((MapPolygon)shapes.get(i)).getFillColor());
				g.fillPolygon((MapPolygon)shapes.get(i));			
			}
		}		
	}
	
	public static void main(String[] args) {
		int typeView = 0; // 0-Full, 1-Solo Aristas Visibles, 2-Solo Todas las Aristas.
		if (args.length!=0) {
			if (!args[0].equalsIgnoreCase("0") && !args[0].equalsIgnoreCase("1") && !args[0].equalsIgnoreCase("2")) {
				System.out.println("El argumento solo puede ser 0, 1 o 2 -0.Caras a Color, 1.Solo aristas visibles, 2.Transparente (todas las aristas visibles)-");
				return;
			}
			typeView = (new Integer(args[0])).intValue();
		}		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final GraficaCubo comp = new GraficaCubo();
		if (args.length==0 || args[0].equalsIgnoreCase("0")) comp.setDrawColor(Color.GRAY);
		//comp.setDrawColor(Color.RED);
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
		PointR3 PVO = new PointR3(comp.POX1, comp.POX2, comp.POX3);
		PointR3 PVOM1, PVOM7; 
		Cubo myCubo = new Cubo(new PointR3(0,0,0), new PointR3(0,0,10), new PointR3(0,10,10), new PointR3(0,10,0), new PointR3(10,0,0), new PointR3(10,10,0), new PointR3(10,10,10), new PointR3(10,0,10));
		double VC[][];
		
	double trasladoCubo[][]={{1,0,0,0.0},{0,1,0,-12.0},{0,0,1,0.0},{0,0,0,1}};
	myCubo = new Cubo((new matrix()).producto(trasladoCubo,myCubo.getHArr()));
	
		int nShapes;
		boolean bSHX1, bSHX2, bSHX3;
		Color face_color[] = /*new Color[]*/ {Color.PINK, Color.GREEN, Color.BLUE, Color.MAGENTA , Color.YELLOW, Color.CYAN}; // El "new Color[]" no sería necesario.
		for (int i=0; i < 14400; i++) {
			nShapes = 0; // Cantidad de formas del cubo dibujadas.
			bSHX1 = bSHX2 = bSHX3 = true; // Repinta el eje (salvo que haya cortado una cara de la figura, o sea tapada por esta).
			
			PVOM1 = PointR3.getPointDiff(PVO, myCubo.getPoint(1));
			PVOM7 = PointR3.getPointDiff(PVO, myCubo.getPoint(7));
			
			/* Producto Mixto, si el resultado es positivo es cara visible, si es menor o igual que cero es cara no visible.
			** Recordar que el producto mixto conjuga un producto vectorial de la 2da y 3er fila y luego el vector resultante (normal a  
			** la cara) es multiplicado escalarmente por la primer fila; en definitiva es lo mismo que calcular el determinante de la matriz.*/	
			double rS[] = new double[] {	(new matrix()).prod_escalar(PVOM1.getArr(), myCubo.getFaceNormal(1).getArr()),
											(new matrix()).prod_escalar(PVOM7.getArr(), myCubo.getFaceNormal(2).getArr()),
											(new matrix()).prod_escalar(PVOM1.getArr(), myCubo.getFaceNormal(3).getArr()),
											(new matrix()).prod_escalar(PVOM1.getArr(), myCubo.getFaceNormal(4).getArr()),
											(new matrix()).prod_escalar(PVOM7.getArr(), myCubo.getFaceNormal(5).getArr()),
											(new matrix()).prod_escalar(PVOM7.getArr(), myCubo.getFaceNormal(6).getArr()) };

			for (int j=1; j<7; j++) {
				if (rS[j-1] >= 0 || typeView == 2) {
					if (typeView == 0) {
						double NS = (new matrix()).prod_escalar(myCubo.getFace(j).getPoint(1).getArr(), myCubo.getFaceNormal(j).getArr());
						double DSX1 = (new matrix()).prod_escalar(new double [][]{{1.0},{0.0},{0.0}}, myCubo.getFaceNormal(j).getArr());
						double DSX2 = (new matrix()).prod_escalar(new double [][]{{0.0},{1.0},{0.0}}, myCubo.getFaceNormal(j).getArr());
						double DSX3 = (new matrix()).prod_escalar(new double [][]{{0.0},{0.0},{1.0}}, myCubo.getFaceNormal(j).getArr());
						PointR3 pCX1 = new PointR3(NS/DSX1, 0.0, 0.0);
						Point pProyCX1 = comp.projectedPoint( pCX1 );
						PointR3 pCX2 = new PointR3(0.0, NS/DSX2, 0.0);
						Point pProyCX2 = comp.projectedPoint( pCX2 );
						PointR3 pCX3 = new PointR3(0.0, 0.0, NS/DSX3);
						Point pProyCX3 = comp.projectedPoint( pCX3 );
						comp.addShape(myCubo.getFace(j), face_color[j-1]);
						nShapes+=1;	
						Polygon POL = (Polygon)comp.getLastShape();
						if (((int)DSX1) != 0 && POL.contains(pProyCX1.getX(),pProyCX1.getY()) && pCX1.getX() > 0) { // Eje X cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX1.getX(): "+pProyCX1.getX()+", pProyCX1.getY(): "+pProyCX1.getY()+"------pCX1.getX(): "+pCX1.getX());
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()-2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX1.getX()-2, (int)pProyCX1.getY()+2, (int)pProyCX1.getX()+2, (int)pProyCX1.getY()-2, Color.BLACK);
							if (DSX1 > 0) {
								comp.addShape((int)pProyCX1.getX(), (int)pProyCX1.getY(), (int)comp.projectedPoint(AX1.getEPoint()).getX(), (int)comp.projectedPoint(AX1.getEPoint()).getY(), Color.RED);
							} else {
								comp.addShape(XO, YO, (int)pProyCX1.getX(), (int)pProyCX1.getY(), Color.RED);
							} 
							bSHX1 = false;
							System.out.println("(S"+j+")Pone bSHX1 a false 1");
							nShapes+=3;
						} else {
							// Punto sobre el eje X para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje X al punto del observador, entonces la cara esta tapando el eje X (X1).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada X como pto. de la recta.
							double x0 = (((int)DSX1) == 0?PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getX():0);
							//double testCoord[] = {PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getX(), 0.0};
							//for (int k=0; k < testCoord.length && bSHX1; k++) {
							//	double x0 =  testCoord[k];
								PointR3 X0 = new PointR3(x0,0,0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myCubo.getFace(j).getPoint(1), X0).getArr(), myCubo.getFaceNormal(j).getArr());
								AristR3 PVO_X0 = new AristR3(X0,PVO);
								double den = (new matrix()).prod_escalar(PVO_X0.getVersor().getArr(), myCubo.getFaceNormal(j).getArr());
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
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+")");
										//comp.addShape((int)X1C_proy.getX()-2, (int)X1C_proy.getY()-2, (int)X1C_proy.getX()+2, (int)X1C_proy.getY()+2, Color.RED);
										//comp.addShape((int)X1C_proy.getX()-2, (int)X1C_proy.getY()+2, (int)X1C_proy.getX()+2, (int)X1C_proy.getY()-2, Color.WHITE);
										//nShapes+=2;
									}
								}								
							//} // End FOR	
						}
						if (((int)DSX2) != 0 && POL.contains(pProyCX2.getX(),pProyCX2.getY()) && pCX2.getY() > 0) { // Eje Y cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX2.getX(): "+pProyCX2.getX()+", pProyCX2.getY(): "+pProyCX2.getY()+"------pCX2.getY(): "+pCX2.getY());
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()-2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX2.getX()-2, (int)pProyCX2.getY()+2, (int)pProyCX2.getX()+2, (int)pProyCX2.getY()-2, Color.BLACK);
							if (DSX2 > 0) {
								comp.addShape((int)pProyCX2.getX(), (int)pProyCX2.getY(), (int)comp.projectedPoint(AX2.getEPoint()).getX(), (int)comp.projectedPoint(AX2.getEPoint()).getY(), Color.GREEN);
							} else {
								comp.addShape(XO, YO, (int)pProyCX2.getX(), (int)pProyCX2.getY(), Color.GREEN);
							} 
							bSHX2 = false;
							System.out.println("(S"+j+")Pone bSHX2 a false 1");
							nShapes+=3;
						} else {
							// Punto sobre el eje Y para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje Y al punto del observador, entonces la cara esta tapando el eje Y (X2).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada Y como pto. de la recta.
							//double y0 = (((int)DSX2) == 0?PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getY():0);
							double y0 = PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getY();
							//double testCoord[] = {PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getY(),0.0};
							//for (int k=0; k < testCoord.length && bSHX2; k++) {
							//	double y0 =  testCoord[k];
								PointR3 Y0 = new PointR3(0,y0,0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myCubo.getFace(j).getPoint(1), Y0).getArr(), myCubo.getFaceNormal(j).getArr());
								AristR3 PVO_Y0 = new AristR3(Y0,PVO);
								double den = (new matrix()).prod_escalar(PVO_Y0.getVersor().getArr(), myCubo.getFaceNormal(j).getArr());
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
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+")");
										//comp.addShape((int)X2C_proy.getX()-4, (int)X2C_proy.getY()-4, (int)X2C_proy.getX()+4, (int)X2C_proy.getY()+4, Color.GREEN);
										//comp.addShape((int)X2C_proy.getX()-4, (int)X2C_proy.getY()+4, (int)X2C_proy.getX()+4, (int)X2C_proy.getY()-4, Color.WHITE);
										//nShapes+=2;
									}
								}
							//} // End FOR
						}
						if (((int)DSX3) != 0 && POL.contains(pProyCX3.getX(),pProyCX3.getY()) && pCX3.getZ() > 0) { // Eje Z cortando cara Sj
							System.out.println("(S"+j+")### - pProyCX3.getX(): "+pProyCX3.getX()+", pProyCX3.getY(): "+pProyCX3.getY()+"------pCX3.getZ(): "+pCX3.getZ());
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()-2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()+2, Color.BLACK);
							comp.addShape((int)pProyCX3.getX()-2, (int)pProyCX3.getY()+2, (int)pProyCX3.getX()+2, (int)pProyCX3.getY()-2, Color.BLACK);
							if (DSX3 > 0) {
								comp.addShape((int)pProyCX3.getX(), (int)pProyCX3.getY(), (int)comp.projectedPoint(AX3.getEPoint()).getX(), (int)comp.projectedPoint(AX3.getEPoint()).getY(), Color.BLUE);
							} else {
								comp.addShape(XO, YO, (int)pProyCX3.getX(), (int)pProyCX3.getY(), Color.BLUE);
							} 
							bSHX3 = false;
							System.out.println("(S"+j+")Pone bSHX3 a false 1");
							nShapes+=3;
						} else {
							// Punto sobre el eje Z para formar una recta con el punto del observador, esta recta cortara el plano de la cara en algun punto.
							// Si el punto de corte esta dentro del poligono de la cara proyectada y si la distancia de este punto al punto del observador es menor 
							// que la distancia del punto sobre el eje Z al punto del observador, entonces la cara esta tapando el eje Z (X3).
							// Tomamos por ejemplo un punto intermedio de 2 vertices opuestos de la cara y de este el valor de su coordenada Z como pto. de la recta.
							//double z0 = (((int)DSX3) == 0?PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getZ():0);
							double testCoord[] = {PointR3.getPointAvg(myCubo.getFace(j).getPoint(1),myCubo.getFace(j).getPoint(3)).getZ(), 0.0};
							for (int k=0; k < testCoord.length && bSHX3; k++) {
								double z0 =  testCoord[k];
								PointR3 Z0 = new PointR3(0,0,z0);
								double num = (new matrix()).prod_escalar(PointR3.getPointDiff(myCubo.getFace(j).getPoint(1), Z0).getArr(), myCubo.getFaceNormal(j).getArr());
								AristR3 PVO_Z0 = new AristR3(Z0,PVO);
								double den = (new matrix()).prod_escalar(PVO_Z0.getVersor().getArr(), myCubo.getFaceNormal(j).getArr());
								double X3CParam = num / den; 
								PointR3 X3C = PointR3.getPointSum(Z0, PVO_Z0.getVersor().escalado(X3CParam)); // Punto de corte en el espacio del plano de la cara con la recta PVO_Z0
								AristR3 PVO_X3C = new AristR3(X3C,PVO);
								System.out.println("(S"+j+")### Dist. z0 a PVO: "+PVO_Z0.getLength()+"Dist. intersecc. S con recta z0-PVO: "+PVO_X3C.getLength());
								if (PVO_Z0.getLength() > PVO_X3C.getLength() && Math.abs(PVO_Z0.getLength()-PVO_X3C.getLength()) > 0.001) { // Posible ocultamiento del eje X3 con cara
									Point X3C_proy = comp.projectedPoint(X3C);
									if (POL.getBounds2D().contains(X3C_proy.getX(),X3C_proy.getY())) {
										System.out.println("(S"+j+")***** - Xproy: "+X3C_proy.getX()+", Yproy: "+X3C_proy.getY()+", Pone bSHX3 a false 2");
										bSHX3 = false;
									} else {
										System.out.println("El punto de corte no caeria dentro de (S"+j+"), eje Z posiblemente no sea ocultado");
										System.out.println("(S"+j+")-Punto que caeria fuera del Poligono: ("+(int)X3C_proy.getX()+","+(int)X3C_proy.getY()+")");
										System.out.println("(S"+j+")-Poligono - P1: ("+((MapPolygon)POL).getXCoordPoint(1)+","+((MapPolygon)POL).getYCoordPoint(1)+") - P2: ("+((MapPolygon)POL).getXCoordPoint(2)+","+((MapPolygon)POL).getYCoordPoint(2)+") - P3: ("+((MapPolygon)POL).getXCoordPoint(3)+","+((MapPolygon)POL).getYCoordPoint(3)+") - P4: ("+((MapPolygon)POL).getXCoordPoint(4)+","+((MapPolygon)POL).getYCoordPoint(4)+")");
										//comp.addShape((int)X3C_proy.getX()-2, (int)X3C_proy.getY()-2, (int)X3C_proy.getX()+2, (int)X3C_proy.getY()+2, Color.BLUE);
										//comp.addShape((int)X3C_proy.getX()-2, (int)X3C_proy.getY()+2, (int)X3C_proy.getX()+2, (int)X3C_proy.getY()-2, Color.WHITE);
										//nShapes+=2;
									} 
								}								
							} // End FOR							
						}					
					}					
					nShapes+=comp.addShapes(myCubo.getFace(j));			
				}				
			}
			
		    if (bSHX1) {
				comp.addShape(AX1, Color.RED);
				nShapes+=1;
			}
			if (bSHX2) {
				comp.addShape(AX2, Color.GREEN);
				nShapes+=1;
			}
			if (bSHX3) {
				comp.addShape(AX3, Color.BLUE);
				nShapes+=1;
			}				
			comp.repaint();
			try{
				Thread.sleep(50);
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}

			if (i < 360) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,1), myCubo.getHArr()); // Gira el cubo un grado en torno del eje X (X1)
			} else if (i < 720) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,1), myCubo.getHArr()); // Gira el cubo un grado en torno del eje Y (X2)
			} else if (i < 1080) {
				VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,1), myCubo.getHArr()); // Gira el cubo un grado en torno del eje Z (X3)
			} else {
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX1,(int)Math.abs(4*Math.sin(comp.grado*i))), myCubo.getHArr()); 
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX2,(int)Math.abs(3*Math.cos(2*comp.grado*i))), VC); 
					VC = (new matrix()).producto((new matrix()).potencia(comp.rotEjeX3,(int)Math.abs(2*Math.sin(3*comp.grado*i))), VC); 
			}			
			myCubo = new Cubo(VC);
			comp.clearLastNShapes(nShapes);
		}
		
		testFrame.setVisible(false); //you can't see me!
		testFrame.dispose(); //Destroy the JFrame object
		return;
	}
}