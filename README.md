*** Archivo en construcción, luego se incluira información mas detallada ***

Descripción de las clases y sus métodos.

matrix.java: Clase que brina el objeto matematico matriz, proporcianando muchos metodos para el uso/manejo de las mismas. En el main de la clase se podrán ver ejemplos de uso.

Forma.java: En este fuente estan las clases que describen los objetos en el espacio.
Contiene las clases:
PointR3 	: Clase que representa un punto en el espacio
			Constructores:
				public PointR3(double x_, double y_, double z_): Input: Las 3 coordenadas cartesianas.
				public PointR3(PointR3 p): Input: Objeto PointR3.
				public PointR3(double[][] pArr): Input: Array con las 3 coordenadas cartesianas.
			Metodos:
				public static PointR3 toCartesian(double rho_, double theta_, double phi_): Input: Coordenadas esfericas. Output: Un punto PointR3.
				public static final PointR3 getPointDiff(PointR3 pEnd, PointR3 pIni): Input: 2 objetos PointR3, Output: Un PointR3 que representa la diferencia (pEnd-Pini).
				public static final PointR3 getPointSum(PointR3 pIni, PointR3 pEnd): Input: 2 objetos PointR3, Output: Un PointR3 que representa la suma (pIni+PFin).
				public static final PointR3 getPointAvg(PointR3 pIni, PointR3 pEnd): Input: 2 objetos PointR3, Output: Un PointR3 que representa el promedio (pIni+PFin)/2.
				public static final PointR3 getProdVect(PointR3 U, PointR3 V): Input: 2 objetos PointR3 (considerados como verctores), Output: Objeto PointR3 que representa el producto vectorial.
				public PointR3 escalado(double escala): Input: doble que representa un factor de escala. Output: pbjeto PointR3 escalado segun el valor de input.
				public double getRho(): Metodo Get que devuelve el valor de la coordenada esferica Rho.
				public double getTheta(): Metodo Get que devuelve el valor de la coordenada esferica Theta.
				public double getPhi(): Metodo Get que devuelve el valor de la coordenada esferica Phi.
				public double getX(): Metodo get que devuelve la coordenada cartesiana X.
				public double getY(): Metodo get que devuelve la coordenada cartesiana Y.
				public double getX(): Metodo get que devuelve la coordenada cartesiana Z.
				public double[][] getArr(): Metodo get que devuelve un array con las coordenadas cartesianas.
				public double[][] getHArr(): Metodo get que devuelve un array con las coordenadas homogeneas.
				
AristR3		: Clase que representa una arista en el espacio.
			Constructores:
				public AristR3(PointR3 pIni_, PointR3 pEnd_): Input: Dos objetos PointR3.
				public AristR3(double xi_, double yi_, double zi_, double xf_, double yf_, double zf_): Input: 6 coordenadas que representan 2 puntos en el espacio.
			Metodos:
				public PointR3 getIPoint(): Metodo Get que retorna el punto inicial (representado por un objeto PointR3) de la arista.
				public PointR3 getEPoint(): Metodo Get que retorna el punto final (representado por un objeto PointR3) de la arista.
				public double getLength(): Metodo Get que retorna la longitud de la arista.
				public PointR3 getVersor(): Metodo que retorna un versor como un objeto PointR3 en la dirección-sentido de la arista.
				
Triangulo	: Clase que representa un triangulo en el espacio.
			Constructores:
				public Triangulo(AristR3 a1_, AristR3 a2_, AristR3 a3_): // A describir
				public Triangulo(PointR3 p1_, PointR3 p2_, PointR3 p3_)
				public Triangulo(double[][] pArr, Color fillColor_)
				public Triangulo(PointR3 p1_, PointR3 p2_, PointR3 p3_, Color fillColor_)
			Metodos:
				public PointR3 getNormal(): Obtiene el vector normal al plano que contiene al triangulo, tomando las aristas como vectores en el sentodo antihorario.
				public boolean esVisible(PointR3 PVO): Input: Objeto PointR3 que representa el punto de vista del observador en el espacio. Output: Booleano "true" si la cara es visible desde el PVO, false si no es visible.
				public PointR3[] getPointR3Arr(): Metodo Get que retorna un array de objetos PointR3 que representan el triangulo en el espacio.
				public PointR3 getPoint(int nPoint): Input: Número de punto del triangulo. Output: Objeto PointR3 del triangulo que es el objeto número "nPoint" del mismo.
				public AristR3 getArist(int nArist): Input: Número de arista del triangulo. Output: Objeto AristR3 del triangulo que es el objeto número "nArist" del mismo.
				public double[][] getHArr(): Metodo Get que retona un array homogeneo de doubles que representan las coordenadas de los puntos en el espacio.
				public Color getFillColor(): Metodo Get que retorna un objeto Color que representa el color de relleno del triangulo al graficarlo.
				public Color getBorderColor(): Metodo Get que retorna un objeto Color que representa el color de borde -perimetral- del triangulo al graficarlo.

Tetraedro: // A describir

Rectangulo: // A describir

Cubo: // A describir

Pentagono: // A describir

Dodecaedro: // A describir

Sphere: // A describir

Perspectiva.java : Clase que extiende de Forma y tiene las matrices de transformación y los métodos para proyectar un punto del espacio sobre el plano, así como tambien las matrices de rotación unitarias alrrededor de los ejes coordenados.
// A describir

Clases que Grafican figuras en el espacio rotando allederor de los ejes, a veces intersectando los mismos (mostrando el punto de interseccion) y rotando aleatoriamente.
GraficaCubo.java: // A describir
GraficaTetraedro.java: // A describir
GraficaDodecaedro.java: // A describir
GraficaEsfera.java: // A describir