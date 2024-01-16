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
/**
Clase matrix conteniendo operaciones comunes sobre matrices
*/
public class matrix {
	/**
	Retorna el producto de Matrices "a * b", pasadas como parametros.
	Recordar que la cantidad de columnas de la matriz "a" debe coincidir con la cantidad de filas de la matriz "b":
	Si la matriz "a" es de M filas por N columnas, la matriz "b" podra ser de N filas por P columnas, o sea:
	MxN * NxP = MxP , M=a.length , N=a[0].length=b.length , P=b[0].length
	*/
	public double [][] producto(double a[][], double b[][]) {
		if (a[0].length != b.length) return null;
		int filas = a.length;
		int columnas = b[0].length;
		double c[][]=new double[a.length][b[0].length];		
		// MxN * NxP = MxP , M=a.length , N=a[0].length=b.length , P=b[0].length
		for (int k=0; k < columnas; k++) {
			for (int i=0; i < filas; i++) {
				c[i][k]=0;
				for (int j=0; j < b.length; j++) {
					c[i][k]+=a[i][j]*b[j][k];
				}
			}
		}	
		return c;
	}

	/**
	Retorna una matriz identidad de la dimensión indicada como parámetro
	*/
    public double [][] identidad(int dim) {
		double c[][]=new double[dim][dim];
		for (int i=0; i < dim; i++) {
			for (int j=0; j < dim; j++) {
				c[i][j] = (i==j?1.0:0.0);
			}
		}
		return c;
	}
	
	/**
	Retorna la matriz resultante de realizar el producto de la matriz "a" por la inversa de "b",
	donde "a" y "b" se pasan como parámetros del método.
	*/
	public double [][] division(double a[][], double b[][]) {
		return producto(a,inversa(b));
	}
	
	/**
	Retorna el producto de la matriz "a" por si misma, tantas veces como indique el parametro "pot".
	*/
	public double [][] potencia(double a[][], int pot) {
		double aux[][];	
		if (pot < 0) {
			if (determinante(a) == 0) return null;
			aux = inversa(a);
			pot = -pot;
		} else {
			aux = a;
		}
		double c[][]=new double[a.length][a.length];		
		c = (pot==0?identidad(a.length):aux);
		for (int i=1; i < pot; i++) {
			c = this.producto(aux,c);
		}
		return c;
	}
	
	/**
	Retorna la matriz suma de las matrices "a" y "b" pasadas como parámetro.
	*/
	public double [][] suma(double a[][], double b[][]) {
		if (a.length != b.length || a[0].length != b[0].length) {
			System.out.println("No es posible sumar matrices de diferente orden o diferente cantidad de filas y columnas");
			return null;
		}
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = a[i][j]+b[i][j];
			}
		}
		return c;
	}

	/**
	Retorna la matriz resta de las matrices "a" y "b" pasadas como parámetro.
	*/	
	public double [][] resta(double a[][], double b[][]) {
		if (a.length != b.length || a[0].length != b[0].length) {
			System.out.println("No es posible restar matrices de diferente orden o diferente cantidad de filas y columnas");
			return null;
		}
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = a[i][j]-b[i][j];
			}
		}
		return c;
	}	

	/**
	Retorna la matriz resultante de multiplicar todos los elementos de "a" por un escalar "escalar"
	*/
	public double [][] escalado(double a[][], double escalar) {
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = escalar*a[i][j];
			}
		}
		return c;	
	}

	/**
	Retorna el valor escalar resultante de realizar el producto escalar de 2 vectores "a" y "b" pasados como parámetro.
	*/	
	public double prod_escalar(double a[][], double b[][]) { 
		if (a.length!=b.length || a[0].length!=b[0].length) return 0.0; // Mejor: Lanzar una excepción
		if (a.length == 1) {
			return producto(a, transpuesta(b))[0][0];
		} else {
			return producto(transpuesta(a),b)[0][0];
		}
	}
	
	/**
	Retorna el valor que representa el determinante de la matriz cuadrada "a" pasada como parametro.
	*/
	public double determinante(double a[][]) {
		double calculo = 0;
		//int submat[][];
		if (a.length == 1) {
			calculo = a[0][0];
		} else {
			for (int col=0; col < a.length; col++) {
				calculo+=(Math.pow(-1,(col/*+2*/))*a[0][col]*determinante(submat(a,0,col)));
			}
		}
		return calculo;
	}
	
	/**
	Retorna la submatriz de la matriz "a" pasada como parámetro, eliminando la fila "fil" y la columna "col" de la
	matriz "a", si "fil" o "col" son -1 significa que no se eliminara fila o columna.
	*/
	public double [][] submat(double a[][], int fil, int col) {
	    if ((fil!=-1 && (a.length-1) < 1) || (col!=-1 && (a[0].length-1) < 1) || fil >= a.length || col >= a[0].length ) return null;
		double c[][] = new double[(fil<0?a.length:a.length-1)][(col<0?a[0].length:a[0].length-1)];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				if (i == fil || j == col) continue;
				c[(fil<0||i<fil?i:i-1)][(col<0||j<col?j:j-1)] = a[i][j];
			}
		}
		return c;
	}

	/**
	Retorna la fila "fila" (index:0) de la matrix "a" pasada como parámetro.
	*/
	public double [][] fila(double a[][], int fila) {
		if (fila > a.length) return null;
		double c[][] = new double[1][a[0].length];
		for (int j=0; j < a[0].length; j++) {
			c[0][j] = a[fila][j];
		}
		return c;
	}

	/**
	Retorna el vector columna "columna" (index:0) de la matrix "a" pasada como parámetro.
	*/	
	public double [][] columna(double a[][], int columna) {
		if (columna > a[0].length) return null;
		double c[][] = new double[a.length][1];
		for (int i=0; i < a.length; i++) {
			c[i][0] = a[i][columna];
		}
		return c;
	}	

	/**
	Retorna un vector fila con la diagonal de la matrix "a" pasada como parámetro.
	el parámetro "distDiagPpal" indica la 'distancia' desde la diagonal principal,-|distDiagPpal|,...,-2,-1,0,1,2,...,+|distDiagPpal|,
	valores mayores que cero son diagonales en la zona triangular superior de la diagonal principal,
	valores menores que cero son diagonales en la zona triangular inferior a la diagonal principal.
	*/		
	public double [][] diagonal (double a[][], int distDiagPpal) {
		if (a.length!=a[0].length || Math.abs(distDiagPpal) >= a.length) {
			System.out.println("Error: No se puede extraer la diagonal solicitada para la matriz informada");
			return null;
		}
		double diag[][] = new double[1][a.length-Math.abs(distDiagPpal)];
		int k = 0;
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				if ((i+distDiagPpal) == j) {
					diag[0][k] = a[i][j];
					k++;
					break; // Mejora 10/08/2023
				}
			}
		}
		return diag;
	}
	
	/**
	Devuelve la suma de los elementos de la diagonal principal de la matriz "a" pasada como parámetro.
	*/
	public double traza(double a[][]) {
		double diag[][] = diagonal(a, 0);
		if (diag == null) {
			System.out.println("Error: No se puede obtener la traza de esta matriz.");
			return 0.0;
		}
		double sumaDiagPpal = 0;
		for (int j=0; j < a.length; j++) {
			sumaDiagPpal += diag[0][j];
		}		
		return sumaDiagPpal;
	}
	
	/**
	Devuelve el booleano true si la matriz es simétrica y false si no lo es.
	*/
	public boolean simetrica (double a[][]) { // Retorna el booleano true si es simetrica y false si no lo es.
		if (a.length!=a[0].length) {
			System.out.print("Error: La matriz informada no es cuadrada, por estructura no es simetrica.-->");
			return false;
		}
		for (int i=1; i < a.length; i++) {
			double diagS[][] = diagonal(a,i);//new double[1][a.length-Math.abs(i)];
			double diagI[][] = diagonal(a,-i);
			for (int j=0; j < diagS[0].length; j++) {
				if (diagS[0][j] != diagI[0][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	Devuelve la matriz cofactor de la matriz "a" pasada como parámetro.
	*/
	public double [][] cofactor(double a[][]) {
		double c[][] = new double[a.length][a.length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a.length; j++) {
				c[i][j]=Math.pow(-1,(i+j/*+2*/))*determinante(submat(a,i,j));
			}
		}	
		return c;
	}

	/**
	Devuelve el rango de la matriz "a" pasada como parámetro
	rango: Número máximo de fílas (o columnas) linealmente independientes.
	*/
    public int rango(double a[][]) {
		//long currentTimeStart = System.currentTimeMillis();
		int maxRg = 0;		
		if (a.length!=a[0].length) {
			System.out.println("Por ahora solo se calculan rangos de matrices cuadradas.");
			return -1;
		} else {
			if (determinante(a)!=0.0) {
				maxRg = a.length;
			} else {
				if (a.length > 1) {
					int valRg = 0;
					for (int i=0; i < a.length && maxRg!=(a.length-1); i++) {
						for (int j=0; j < a.length && maxRg!=(a.length-1); j++) {
							if((valRg=rango(submat(a,i,j)))!=0 && valRg>maxRg) {
								maxRg=valRg;
							}
						}
					} 
				}
			}
		}
		//long currentTimeEnd = System.currentTimeMillis();
		//long duracion = currentTimeEnd-currentTimeStart;
		//
		//if (duracion > 0L) System.out.println("Duracion: "+duracion);
		return maxRg;
	}
	
	/**
	Retorna la triangularización en matriz triangular superior de la matriz "a" pasada como parámetro.
	*/
	public double [][] triangularSup(double a[][]) {
		if (a.length!=a[0].length) {
			System.out.println("Solo se triangularizan matrices cuadradas.");
			return null;
		}
		double c[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			System.arraycopy(a[i], 0, c[i], 0, a[i].length);
		}		
		for (int j=0; j < c.length; j++) {
			 for (int i=j; i < c.length; i++){
				if (i==j && c[i][j]==0.0) {
					int k = 0;
					for (k=i+1; k < c.length && c[k][j]==0.0; k++);
					//System.out.println("k:"+k);
					if (k < c.length) {
						for (int m=0; m < c.length; m++) {
							c[i][m] = c[i][m] + c[k][m];
						}
					} else {
						System.out.println("La matriz no es triangularizable.");
						return null;						
					}
				}
				if (i==j) {
					for (int k=i+1; k < c.length; k++) {
						if (c[k][j]!=0.0) {
							double piv = c[k][j];
							for (int m=j; m < c.length; m++ ){
								c[k][m] = ((-piv/c[i][j])*c[i][m]) + c[k][m];
							}
						}
					}						
				}
			}
		}	
		return c;
	}

	/**
	Retorna la triangularización en matriz triangular inferior de la matriz "a" pasada como parámetro.
	*/
	public double [][] triangularInf(double a[][]) {
		if (a.length!=a[0].length) {
			System.out.println("Solo se triangularizan matrices cuadradas.");
			return null;
		}
		double c[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			System.arraycopy(a[i], 0, c[i], 0, a[i].length);
		}		
		for (int j=c.length-1; j >= 0; j--) {
			 for (int i=j; i >= 0; i--){
				if (i==j && c[i][j]==0.0) {
					int k = 0;
					for (k=i-1; k >= 0 && c[k][j]==0.0; k--);
					//System.out.println("k:"+k);
					if (k >= 0) {
						for (int m=0; m < c.length; m++) {
							c[i][m] = c[i][m] + c[k][m];
						}
					} else {
						System.out.println("La matriz no es triangularizable.");
						return null;						
					}
				}
				if (i==j) {
					for (int k=i-1; k >= 0; k--) {
						if (c[k][j]!=0.0) {
							double piv = c[k][j];
							for (int m=j; m >= 0; m-- ){
								c[k][m] = ((-piv/c[i][j])*c[i][m]) + c[k][m];
							}
						}
					}						
				}
			}
		}	
		return c;
	}
	
	/**
	Retorna la matriz transpuesta de la matriz "a" pasada como parámetro.
	*/
	public double [][] transpuesta(double a[][	]) {
		double c[][] = new double[a[0].length][a.length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[j][i]=a[i][j];
			}
		}	
		return c;
	}

	/**
	Retorna la matriz adjunta de la matriz "a" pasada como parámetro.
	*/
	public double [][] adjunta(double a[][]) {
		return transpuesta(cofactor(a));
	}

	/**
	Retorna la matriz inversa de la matriz "a" pasada como parámetro.
	*/
	public double [][] inversa(double a[][]) {
		double det = determinante(a);
		double c[][] = adjunta(a);
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a.length; j++) {
				c[i][j]=c[i][j] / det;
			}
		}	
		return c;		
	}

	/**
	Retorna una matriz con los vectores normalizados de la matriz "mat" pasada como parámetro.
	*/
	double [][] getMatVecsNormalized(double mat[][]){
		double sumpot;
		int fil;
		for (int col=0; col < mat[0].length; col++) {
			sumpot = 0;
			for (fil=0; fil<mat.length; fil++) {
				sumpot += Math.pow(mat[fil][col],2);
			}
			for (fil=0; fil<mat.length; fil++) {
				mat[fil][col] = mat[fil][col] / Math.sqrt(sumpot);
			}
		}
		return mat;
	}	

	/**
	Clase que pone en práctica la descomposición LU, pudiendo obtenerse las dos matrices que se generan con el algoritmo LU.
	*/
	public class descomposicionLU {
		double L[][],U[][];
		public descomposicionLU(double mat[][]) {
			this.U = (new matrix()).triangularSup(mat);
			if (this.U != null) {
				this.L = (new matrix()).roundMat((new matrix()).producto(mat,(new matrix()).inversa(this.U))); // L = MAT*U^-1 = L*U*U^-1
			} else {
				this.L = null;
			}
		}
		/**
		Retorna la matriz "L" de la decomposición LU de la matriz pasada en el constructor.
		*/
		public double [][] getL() {
			return this.L;
		}
		/**
		Retorna la matriz "U" de la decomposición LU de la matriz pasada en el constructor.
		*/
		public double [][] getU() {
			return this.U;
		}
	}
	
	/**
	Clase que pone en práctica el algoritmo de Jacobi para obtener los autovalores y si es posible los autovectores de una Matriz.
	*/
	public class diagonalizadorJacobi {
		final double TOPE_MINIMO = 1e-8;
		double matDiag[][];
		double matVecP[][];
		double x,y,z,c,s;
		public diagonalizadorJacobi(double mat[][], int maxIter) {
			int contador = 0;
			double maximo, tolerancia, suma_sqr;
			int i,j,k,l;
			matDiag = new double[mat.length][mat[0].length];
			for (i = 0; i < mat.length; i++) {
				System.arraycopy(mat[i], 0, matDiag[i], 0, mat[i].length);
			}				
			double p[][] = new double[mat.length][mat[0].length];
			matVecP = (new matrix()).identidad(mat.length); // Intenta luego contener los Vectores Propios
			do {
				k= 0; l=1;
				maximo = Math.abs(matDiag[k][1]);
				for(i=0; i<mat.length-1; i++){
					for(j=i+1; j<mat.length; j++){
						if(Math.abs(matDiag[i][j])>maximo){
							k=i;        
							l=j;
							maximo=Math.abs(matDiag[i][j]);
						}
					}
				}
				suma_sqr = 0.0;
				for(i=0; i<mat.length; i++){
					suma_sqr+=matDiag[i][i]*matDiag[i][i];
				}
				tolerancia=0.0001*Math.sqrt(suma_sqr)/mat.length;
				if(maximo < tolerancia) break;
				p = (new matrix()).identidad(mat.length);
				y=matDiag[k][k]-matDiag[l][l];
				if(Math.abs(y)<TOPE_MINIMO){
					c=s=Math.sin(Math.PI/4);
				}else{
					x=2*matDiag[k][l];
					z=Math.sqrt(x*x+y*y);
					c=Math.sqrt((z+y)/(2*z));
					s=((x/y)/Math.abs(x/y))*Math.sqrt((z-y)/(2*z));
				}
				p[k][k]=c;
				p[l][l]=c;
				p[k][l]=s;
				p[l][k]=-s;
				matDiag=(new matrix()).producto(p, (new matrix()).producto(matDiag, (new matrix()).transpuesta(p)));
				matVecP=(new matrix()).producto(matVecP, (new matrix()).transpuesta(p));
				contador++;	
			}while (contador < maxIter);
			if(contador==maxIter){
				System.out.println("Imposible calcular los valores propios");
			}
			matDiag = (new matrix()).roundMat(matDiag);
			matVecP = (new matrix()).roundMat(matVecP);			
		}
		/**
		Retorna una matriz que contiene en la diagonal los valores propios de una matriz pasada en el constructor
		*/
		public double [][] getMatValPr() {
			return this.matDiag;
		}
		/**
		Retorna una matriz cuyas columnas son los vectores propios de una matriz pasada en el constructor
		*/		
		public double [][] getMatVecPr() {
			return this.matVecP;
		}
		/**
		Retorna un vector con los autovalores de la matriz pasada en el constructor
		*/
		public double [][] getAutovalores() {
			return (new matrix()).diagonal(this.matDiag, 0);
		}		
	}
	
	/**
	Retorna una matriz con la ortonormalización Gram-Schmidt de la matriz "mat" pasada como parámetro.
	*/
	public double [][] ortonormalizacionGS (double mat[][]) {
		int i;
		double mon[][] = new double[mat.length][mat[0].length];
		for (i = 0; i < mat.length; i++) {
			System.arraycopy(mat[i], 0, mon[i], 0, mat[i].length);
		}
		for (int j = 0; j < mat[0].length; j++) {
			double aux[][] = (new matrix()).columna(mat,j);
			double aux1[][]	= new double[mat.length][1];
//			for (i=0; i < mat.length; i++) {
//				aux1[i][0] = 0.0;
//			}
			for (int k = 0; k < j; k++) {
				aux1 = (new matrix()).resta(aux1,(new matrix()).escalado((new matrix()).columna(mon,k),(new matrix()).prod_escalar(aux,(new matrix()).columna(mon,k))));
			}
			double sumpot = 0;
			aux = (new matrix()).suma(aux,aux1);
			for (int m=0; m<mat.length; m++) {
				sumpot += Math.pow(aux[m][0],2);
			}
			aux = (new matrix()).escalado(aux, (1.0/Math.sqrt(sumpot)));
			for (i=0; i < mat.length; i++) {
				mon[i][j] = aux[i][0];
			}			
		}
		return mon;
	}

	/**
	Clase que pone en práctica el algoritmo QR
	*/
	public class AlgoritmoQR {
		double Q[][];
		double R[][];
		double Qprod[][];
		double matOri[][];
		public AlgoritmoQR(double mat[][], int maxIter) {
			Qprod = (new matrix()).identidad(mat.length);
			matOri = mat;
			calcula(mat, maxIter);
		}
		/**
		Método privado que implementa el algoritmo QR.
		*/
		private void calcula(double mat[][], int maxIter) {
			if (maxIter <= 0) return;
			Q = (new matrix()).ortonormalizacionGS(mat);
			R = (new matrix()).producto((new matrix()).transpuesta(Q), mat);
			Qprod = (new matrix()).producto(Qprod, Q);
			calcula((new matrix()).producto(R,Q), maxIter-1);
		}
		/**
		Retorna la matriz "Q" del último paso del algoritmo QR
		*/
		public double [][] getMatQ() {
			return (new matrix()).roundMat(this.Q);
		}
		/**
		Retorna la matriz "R" del último paso del algoritmo QR
		*/		
		public double [][] getMatR() {
			return (new matrix()).roundMat(this.R);
		}
		/**
		Retorna una matriz con los vectores propios normalizados de la matriz pasada en el constructor.
		*/			
		public double [][] getMatVecPr() {
			// Nota: Uso parcial del método de Leverrier para calcular los vectores propios (ya que los valores propios los teníamos).
			double [][]avalp = getAutovalores2();
			double [][]avecp = new double[matOri.length][matOri[0].length];
			double [][]X2n = new double[matOri.length-1][1];
			double [][]terInd = (new matrix()).escalado((new matrix()).submat((new matrix()).columna(matOri,0),0,-1),-1);
			for (int i=0; i < avalp[0].length; i++){
				double [][]matWork = (new matrix()).submat(matOri,0,0);
				for (int j = 0; j < matWork.length; j++){
					matWork[j][j] -= avalp[0][i];
				}
				X2n = (new matrix()).producto((new matrix()).inversa(matWork),terInd);
				avecp[0][i] = 1;
				for (int j = 1; j < avecp.length; j++) {
					avecp[j][i] = X2n[j-1][0];
				}
			}
			return (new matrix()).getMatVecsNormalized(avecp);
		}
		/**
		Retorna un vector con los autovalores de la matriz pasada en el constructor.
		*/
		public double [][] getAutovalores() {
			return (new matrix()).roundMat((new matrix()).diagonal((new matrix()).producto(this.Q,this.R), 0));
		}
		/**
		Retorna un vector con los autovalores de la matriz pasada en el constructor.
		*/		
		public double [][] getAutovalores2() { // De preferencia (aunque el resultado es muy similar a getAutovalores())
			return (new matrix()).roundMat((new matrix()).diagonal((new matrix()).producto((new matrix()).transpuesta(this.Qprod),(new matrix()).producto(this.matOri,this.Qprod)), 0));
		}			
	}
	
	/**
	Imprime la matriz pasada como parámetro.
	*/
	static void printMat (double c[][]) { // Solo imprime la Matriz de forma "legible".
		for (int fila=0; fila < c.length; fila++) {
			for (int col=0; col < c[0].length; col++) {
				System.out.print("C("+(fila+1)+","+(col+1)+")="+c[fila][col]+"  ");
			}
			System.out.println();
		}		
	}
	
	/**
	Redondea los elementos de la matriz pasada como parámetro.
	*/
	public double [][] roundMat (double a[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int fila=0; fila < a.length; fila++) {
			for (int col=0; col < a[0].length; col++) {
				double dDiff = Math.abs(a[fila][col] - (double)Math.round(a[fila][col]));
				if ( dDiff < 0.0001 ) {
					c[fila][col] = (double)Math.round(a[fila][col]);
				} else {
					double aux = 0.0;
					int i = 0;
					for (i = 1; i < 5; i++) {
						aux = Math.pow(10.0,(double)i) * a[fila][col];
						dDiff = Math.abs(aux - (double)Math.round(aux));
						if (dDiff < 0.0001) break;
					}
					if (i==5) {
						c[fila][col] = a[fila][col];
					} else {
						c[fila][col] = Math.round(aux) / Math.pow(10.0,(double)i);
					}
				}
			}
		}	
		return c;
	}

	/**
	Otro método para redondear los elementos de una matriz pasada como parámetro.
	*/
	public double [][] roundMat2 (double a[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int fila=0; fila < a.length; fila++) {
			for (int col=0; col < a[0].length; col++) {
				double dDiff = 0.0, aux = 0.0;
				int i;
				for (i = 0; i < 7; i++) {
					aux = Math.pow(10.0,(double)i) * a[fila][col];
					dDiff = Math.abs(aux - (double)Math.round(aux));
					if (dDiff < 0.0000001) break;
				}
				if (i==7) {
					c[fila][col] = a[fila][col];
				} else {
					c[fila][col] = Math.round(aux) / Math.pow(10.0,(double)i);
				}	
			}
		}	
		return c;
	}
	
public static void main(String [] args) {
		double a[][]={{1,1,1,1,1},{-1,2,0,0,0},{0,0,3,0,0},{0,0,0,4,0},{0,0,0,0,5}};
		double b[][]={{1,0,0,0,0},{0,2,0,0,0},{0,0,3,0,0},{0,0,0,4,0},{0,0,0,0,5}};
		double d[][]={{1,1,1,1,1},{-1,2,-3,1,-2}};
		double e[][]={{1,0},{2,2},{3,2},{4,1},{5,2}};
		double f[][]={{1,0,0,0,0},
					  {1,0,1,0,1},
					  {1,0,0,0,0},
					  {0,0,0,0,1},
					  {0,0,0,0,1}};
		double g[][]={{-26,-33,-25},
					  {31,42,23},
					  {-11,-15,-4}};					  
		double c[][]=new double[5][5]; //5 rows and 5 columns
		System.out.println("Matriz A: ");
		printMat(a);
		System.out.println("Matriz B: ");
		printMat(b);		
		System.out.println("Producto AxB=");
		printMat((new matrix()).producto(a,b));

		System.out.println("POTENCIA 10 de A:");
		printMat((new matrix()).potencia(a,10));

		System.out.println("Diagonal Principal de Matriz A: ");
		printMat((new matrix()).diagonal(a,0));
		System.out.println("Diagonal Primera superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,1));
		System.out.println("Diagonal Primera inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-1));			
		System.out.println("Diagonal Segunda superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,2));
		System.out.println("Diagonal Segunda inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-2));
		System.out.println("Diagonal Tercera superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,3));
		System.out.println("Diagonal Tercera inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-3));
		System.out.println("Diagonal Cuarta superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,4));
		System.out.println("Diagonal Cuarta inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-4));		
		System.out.println("Matriz A "+((new matrix()).simetrica(a)?"es Simetrica":"NO es simertrica"));
		System.out.println("Matriz B "+((new matrix()).simetrica(b)?"es Simetrica":"NO es simertrica"));
		System.out.println("Matriz D "+((new matrix()).simetrica(d)?"es Simetrica":"NO es simertrica"));		
		System.out.println("Matriz F "+((new matrix()).simetrica(f)?"es Simetrica":"NO es simertrica"));
		System.out.println("Submat de A eliminando fila 2 columna 2 (esto es DISPLAY, recordar que internamente filas y columnas empiezan en 0): ");	
		printMat((new matrix()).submat(a, 1, 1));
		System.out.println("Matriz transpuesta de Matriz A: ");
		printMat((new matrix()).transpuesta(a));	
		System.out.println("Determinante de A: "+(new matrix()).determinante(a));
		System.out.println("Matriz cofactor de A: ");
		printMat((new matrix()).cofactor(a));
		System.out.println("Matriz adjunta de A: ");
		printMat((new matrix()).adjunta(a));
		System.out.println("Matriz inversa de A: ");
		printMat((new matrix()).inversa(a));
		System.out.println("MULTIPLICA  A^-1xA:");
		printMat((new matrix()).producto((new matrix()).inversa(a), a));
		System.out.println("Matriz D: ");
		printMat(d);
		System.out.println("Matriz transpuesta de D: ");
		printMat((new matrix()).transpuesta(d));
		System.out.println("Matriz E: ");
		printMat(e);	
		System.out.println("Producto DxE=");
		printMat((new matrix()).producto(d,e));	
		System.out.println("Producto ExD=");
		printMat((new matrix()).producto(e,d));			
		System.out.println("DIVISION  A/A:");
		printMat((new matrix()).division(a, a));
		System.out.println("Rango de Matriz A:"+(new matrix()).rango(a));
		System.out.println("Rango de Matriz B:"+(new matrix()).rango(b));
		System.out.println("Rango de Matriz E:"+(new matrix()).rango(e));
		System.out.println("Rango de Matriz F:"+(new matrix()).rango(f));
		System.out.println("Suma A+B=");
		printMat((new matrix()).suma(a,b));	
		System.out.println("Resta A-B:");
		printMat((new matrix()).resta(a, b));
		System.out.println("##### CAMBIO DE BASES #####");
		double BV[][]={{1,0,1},{0,2,0},{0,0,3}}, BN[][]={{2,1,0},{0,1,3},{1,0,2}}, VC1[][]={{7},{5},{2}};
		System.out.println("Base BV: ");
//		System.out.println(""+BV[0][0]+","+BV[0][1]+","+BV[0][2]);
		printMat((new matrix()).fila(BV,0));
		printMat((new matrix()).fila(BV,1));
		printMat((new matrix()).fila(BV,2));
		System.out.println("Base BN: ");
		printMat((new matrix()).fila(BN,0));
		printMat((new matrix()).fila(BN,1));
		printMat((new matrix()).fila(BN,2));
		System.out.println("Matriz Forward de la transformacion - Canonica BC<=>BV:");
		printMat((new matrix()).transpuesta(BV));	
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BV:");
		printMat((new matrix()).inversa((new matrix()).transpuesta(BV)));
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BV(Idem anterior con redondeo):");
		printMat((new matrix()).roundMat((new matrix()).inversa((new matrix()).transpuesta(BV))));
		System.out.println("Matriz Forward de la transformacion - Canonica BC<=>BN:");		
		printMat((new matrix()).transpuesta(BN));	
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BN:");
		printMat((new matrix()).inversa((new matrix()).transpuesta(BN)));		
		System.out.println("VC1: Vector -en base canonica BC- a transformar: ");
		printMat(VC1);	
		System.out.println("VBV: Vector VC1 transformado a BV (Matriz Backward de transformación BC<=>BV x Vector VC1): ");
		double VBV[][] = (new matrix()).producto((new matrix()).inversa((new matrix()).transpuesta(BV)),VC1);
		printMat(VBV);
		System.out.println("VBVaC:Vector VBV transformado a BC (Matriz Forward de transformación BC<=>BV x Vector VBV): ");
		double VBVaC[][] = (new matrix()).producto((new matrix()).transpuesta(BV),VBV);
		printMat(VBVaC);
 
		System.out.println("BVaBN_bw: Matriz Backward de la transformacion - BV<=>BN: ");
		double BVaBN_bw[][] = (new matrix()).producto((new matrix()).inversa((new matrix()).transpuesta(BN)),(new matrix()).transpuesta(BV));
		printMat(BVaBN_bw);
        System.out.println("BVaBN_fw: Matriz Forward de la transformacion - BV<=>BN: ");
		double BVaBN_fw[][] = (new matrix()).inversa(BVaBN_bw);
		printMat(BVaBN_fw);	
		/*System.out.println("BVaBN_fw: REDONDEADA");
		BVaBN_fw = (new matrix()).roundMat2(BVaBN_fw);
		printMat(BVaBN_fw); */
		
		System.out.println("VBN: Vector transformado de BV a BN (con Matriz Backward de transformacion BV<=>BN): ");
		double VBN[][] = (new matrix()).producto(BVaBN_bw,VBV);
		printMat(VBN);
		System.out.println("VBNaBV: Vector transformado de BN a BV (con Matriz Forward de transformacion BV<=>BN): ");
		printMat((new matrix()).producto(BVaBN_fw,VBN)); ///
		System.out.println("VBNaC: Vector transformado de BN a Base Canonica (con Matriz Forward de transformacion BC<=>BN): ");
		double VBNaC[][] = (new matrix()).producto((new matrix()).transpuesta(BN),VBN);
		printMat(VBNaC);
		System.out.println("Idem anterior: Vector transformado de BN a Base Canonica (REDONDEANDO): ");
		printMat((new matrix()).roundMat2(VBNaC));
		System.out.println("##### FIN CAMBIO DE BASES #####");	
		System.out.println("### INICIO EJEMPLO Solucion de sistema de ecuaciones #####");
		double h[][]={{1,1,0},{0,2,1},{2,0,3}}, x[][]={{1},{2},{5}};
		System.out.println("Matriz de Coeficientes: ");
		printMat(h);
		System.out.println("Matriz de Terminos Independientes: ");
		printMat(x);
		System.out.println("Matriz Solucion: ");
		printMat((new matrix()).producto((new matrix()).inversa(h),x));
		System.out.println("### FIN EJEMPLO Solucion de sistema de ecuaciones #####");
		System.out.println("Traza de Matriz A: "+(new matrix()).traza(a));
		System.out.println("Matriz M:");
		double m[][]={{1,1,1},{1,1,0},{1,0,0}}, n[][]={{1,1,0},{1,-1,1},{1,0,-1}};
		printMat(m);
		System.out.println("Sub-Matriz M sacando fila 2:");
		printMat((new matrix()).submat(m, 1, -1));
		System.out.println("Matriz Inversa de M:");
		printMat((new matrix()).inversa(m));
		System.out.println("----------------");
		printMat((new matrix()).producto((new matrix()).producto((new matrix()).inversa(m),n),m));
		System.out.println("----------------");		
		System.out.println("Matriz G: ");
		printMat(g);
		System.out.println("Matriz con autovector de G: ");
		printMat((new matrix()).producto((new matrix()).resta(g,(new matrix()).escalado((new matrix()).identidad(3),15/*(new matrix()).triangularSup(g)[0][0]*/)),(new matrix()).resta(g,(new matrix()).escalado((new matrix()).identidad(3),-6/*(new matrix()).triangularSup(g)[1][1]*/))));
		System.out.println("Determinante de G: "+(new matrix()).determinante(g));

		System.out.println("Matriz triangular sup. para G: ");
		double gt[][]=(new matrix()).triangularSup(g);
		printMat(gt);
		System.out.println("Determinante de Mat Triang Sup para G: "+(new matrix()).determinante(gt));

		System.out.println("Matriz triangular Inf. para G: ");
		double gti[][]=(new matrix()).roundMat2((new matrix()).triangularInf(g));
		printMat(gti);
		System.out.println("Determinante de Mat Triang Inf para G: "+(new matrix()).determinante(gti));		

		System.out.println("Matriz Diagonal??? (aplicando triangularización superior e inferior sucesivamente):");
		double gtd[][]=(new matrix()).roundMat2((new matrix()).triangularInf(gt));
		printMat(gtd);		
		System.out.println("Matriz Diagonal??? (aplicando triangularización inferior y superior sucesivamente):");
		double gtd2[][]=(new matrix()).roundMat2((new matrix()).triangularSup(gti));
		printMat(gtd2);
		
		System.out.println("--------------------------------------------Descomposicion LU------------------------------------");
		descomposicionLU dLU = (new matrix()).new descomposicionLU(g);
		System.out.println("(L) Matriz L de la descomposicion LU: ");
		printMat(dLU.getL());
		System.out.println("(U) Matriz U de la descomposicion LU: ");		
		printMat(dLU.getU());
		System.out.println("Reconstruccion Matriz Original G a partir de producto LU: ");
		printMat((new matrix()).roundMat((new matrix()).producto(dLU.getL(),dLU.getU())));		
		System.out.println("------------------------------------------FIN Descomposicion LU-----------------------------------");
		System.out.println("------------------------------------------Ejemplos de Diagonalización x Jacobi------------------------------------------------------------------------");		
		diagonalizadorJacobi diag = (new matrix()).new diagonalizadorJacobi(g, 20);
		System.out.println("Matriz Diagonal (o con Valores propios en la diagonal) de g:");
		printMat(diag.getMatValPr());
		System.out.println("Autovalores:");
		printMat(diag.getAutovalores());		
		System.out.println("Matriz de Vectores Propios:");
		printMat(diag.getMatVecPr());
		double E[][]={{7,-1,-1},
			  {-1,5,1},
			  {-1,1,5}};
		System.out.println("Matriz E (mayuscula):");
		printMat(E);
		double Et[][]=(new matrix()).triangularSup(E);
		diag = (new matrix()).new diagonalizadorJacobi(E, 20);
		System.out.println("Matriz Diagonal:");
		printMat(diag.getMatValPr());
		System.out.println("Autovalores:");
		printMat(diag.getAutovalores());
		System.out.println("Matriz de Vectores Propios:");
		printMat(diag.getMatVecPr());		
		System.out.println("Matriz : E * MatVecPr (cada columna N de la matriz resultante es = Valor propio N x Vector propio N:");
		printMat((new matrix()).producto(E,diag.getMatVecPr()));
		
		double F[][]={{-3,2,0,0},
			  {-3,4,0,0},
			  {0,0,-5,-4},
			  {0,0,-2,2}};
		System.out.println("Matriz F (mayuscula):");
		printMat(F);
		diag = (new matrix()).new diagonalizadorJacobi(F, 20);
		System.out.println("Matriz Diagonal:");
		printMat(diag.getMatValPr());
		System.out.println("Autovalores:");
		printMat(diag.getAutovalores());		
		System.out.println("Matriz de Vectores Propios:");
		printMat(diag.getMatVecPr());	

		double H[][]={{0,0,1,0},
			  {0,0,0,1},
			  {1,0,0,0},
			  {0,1,0,0}};
		System.out.println("Matriz H:");
		printMat(H);
		diag = (new matrix()).new diagonalizadorJacobi(H, 20);
		System.out.println("Matriz Diagonal:");
		printMat(diag.getMatValPr());
		System.out.println("Autovalores:");
		printMat(diag.getAutovalores());
		System.out.println("Matriz de Vectores Propios:");
		printMat(diag.getMatVecPr());
		System.out.println("-------------------------- Descomposicion QR de una Matriz --------------------------------");
/* 		double L[][]={{1,1,1},
			  {0,1,1},
			  {1,0,-1}}; */
		double L[][]={{12,-51,4},
			  {6,167,-68},
			  {-4,24,-41}};			  
		System.out.println("Matriz Original L (mayuscula)");
		printMat(L);
		System.out.println("Matriz Ortonormalizada o Matriz Q de la descomposicion QR:");
		double Q[][] = (new matrix()).ortonormalizacionGS(L);
		printMat(Q);
		System.out.println("Matriz R de la descomposición QR:");
		double R[][] = (new matrix()).roundMat((new matrix()).producto((new matrix()).transpuesta((new matrix()).ortonormalizacionGS(L)), L));
		printMat(R);
		System.out.println("Comprobación QxR = Matriz Original:");
		printMat((new matrix()).roundMat((new matrix()).producto(Q,R)));
		System.out.println("----------------------Ejemplos Val y Vec Propios con Algoritmo QR + Leverrier (este ultimo solo para Calcular los vectores propios)---------------------------------");		
		double K[][]={{-26,-33,-25},
			  {31,42,23},
			  {-11,-15,-4}};	
		System.out.println("Matriz L (mayuscula):");
		printMat(L);
		AlgoritmoQR algQR = (new matrix()).new AlgoritmoQR(L,50);
		System.out.println("Matriz R último paso:");
		printMat(algQR.getMatR());
		System.out.println("Matriz Q último paso:");
		printMat(algQR.getMatQ());		
		System.out.println("Autovalores:");
		double[][] valp = algQR.getAutovalores2();
		//printMat(algQR.getAutovalores());
		printMat(valp);	// Metodo 2
		System.out.println("Matriz de Vectores Propios:");
		printMat(algQR.getMatVecPr());
		System.out.println("---------------------------------OkOkOk---Comprobación VecPr---OkOkOk---------------------------------------------------------------");		
		System.out.println("ValP1: "+valp[0][0]);
		printMat((new matrix()).columna(algQR.getMatVecPr(),0));
		printMat((new matrix()).producto(L,(new matrix()).columna(algQR.getMatVecPr(),0))); // L*VecPn = ValPn*VecPn donde n = 0,1, 2...n
 		System.out.println("-------2--------");
		System.out.println("ValP2: "+valp[0][1]);
		printMat((new matrix()).columna(algQR.getMatVecPr(),1));	
		printMat((new matrix()).producto(L,(new matrix()).columna(algQR.getMatVecPr(),1)));
		System.out.println("--------3-------");
		System.out.println("ValP3: "+valp[0][2]);
		printMat((new matrix()).columna(algQR.getMatVecPr(),2));		
		printMat((new matrix()).producto(L,(new matrix()).columna(algQR.getMatVecPr(),2))); 
		System.out.println("------------------------------------------Otro Ejemplo de Diagonalización x Jacobi------------------------------------------------------------------------");		
		diagonalizadorJacobi diagJ = (new matrix()).new diagonalizadorJacobi(L, 50);
		System.out.println("Matriz Diagonal (o con Valores propios en la diagonal):");
		printMat(diagJ.getMatValPr());
		System.out.println("Autovalores:");
		printMat(diagJ.getAutovalores());		
		System.out.println("Matriz de Vectores Propios:");
		printMat(diagJ.getMatVecPr());
		System.out.println("--------------------Comprobación VecPr (Jacobi)-------(OK cuando Mat original es simetrica, no siempre cuando no lo es)-------------------------------------------");		
		printMat((new matrix()).columna(diagJ.getMatVecPr(),2));
		printMat((new matrix()).producto(L,(new matrix()).columna(diagJ.getMatVecPr(),2)));		// L*VecP2 = ValP2*VecP2
		//System.out.println("---------------");
		//printMat((new matrix()).roundMat((new matrix()).producto(algQR.getMatVecPr(),(new matrix()).transpuesta(algQR.getAutovalores()))));		
		//printMat(Et);		
		/*double g[][]={{1.0,2.0},{-1.0,3.0}}, q[][];
		System.out.println("Matriz G y Su inversa:");
		printMat(g);
		printMat((new matrix()).inversa(g));
		q = g;
		q[0][0] = q[1][1] = 5.0;
		System.out.println("Matriz G (modificada a traves de una referencia):");
		printMat(g);*/
	}
}