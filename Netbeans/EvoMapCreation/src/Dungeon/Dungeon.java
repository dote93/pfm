package Dungeon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Dungeon.Celda.Tipo_puertas;

/**
 * Clase que gestiona el dungeon
 *
 * @version 1.0
 * @author Alvaro Valle del Pozo TFM Profesor: Daniel Manrique y Jose Maria Font
 * Fecha: 01-02-2016
 */
public class Dungeon implements Cloneable {

    int f;
    int c;
    int filas_iniciales = 0;
    int columnas_iniciales = 0;

    //FENOTIPO INDIVIDUO
    public Celda[][] dungeon;

    //GENOTIPO INDIVIDUO
    public int[] genotipo; // array que va a almacenar el genotipo del invididuo 

    //Variable para saber cuantos tipos de celdas va a haber, es decir, el esponente de 2 elevado a N
    public int tipo_celdas;

    public int numero_monstruos = 0;
    public int numero_tesoros = 0;

    public int numero_puertas = 0;

    //TODO VER SI SE PUEDE HACER EL PASO DE LAS PUERTAS DE OTRA MANERA 
    public ArrayList<int[]> pos_puertas;

    public ArrayList<Tipo_puertas> t_puertas;

    public int celdas_Paredes = 0; //variable para saber cuantas paredes se han colocado en el mapa

    public int celdas_Vacias = 0; //variable para saber cuantas celdas vacias se han colocado en el mapa

    //Variable para guardar cuanto porcenaje queremos que haya de celdas vacias
    int porcentaje = 0;

    //Variable para guardar cuanto porcenaje queremos que haya de celdas pared
    int porcentaje_paredes = 0;

    //Variable para saber la posicion de donde estan las paredes
    ArrayList<Celda> posicion_puertas = new ArrayList<Celda>();

    //Variable para saber la posicion de donde estan los tesoros
    ArrayList<Celda> posicion_tesoros = new ArrayList<Celda>();

    //Variable para saber la posicion de donde estan los monstruos
    ArrayList<Celda> posicion_monstruos = new ArrayList<Celda>();

    //Arraylist de las distancias minimas PT de todas las puertas
    ArrayList<Integer> distancia_min_PT = new ArrayList<Integer>();

    //Arraylist de las distancias minimas PM
    ArrayList<Integer> distancia_min_PM = new ArrayList<Integer>();

    //Fitness del individuo
    public double fitness = 0.0;

    //booleano para saber si ya se ha llegado a la meta y si es asi ya no se tiene que seguir pasando por la funcion
    boolean llegada = false;

    //Variable para calcular un numero aleatorio entre el numero de filas o columnas y 0
    int min = 0;

    //Variables para saber si ya hay una puerta en ese lado
    boolean hay_puerta_N;
    boolean hay_puerta_S;
    boolean hay_puerta_E;
    boolean hay_puerta_O;

    //Distancia entre una celda P y una T
    public int distancia = -1;

    //Variable para saber si el dungeon es valido o no
    public boolean dungeon_valido;

    //variable que va a guardar la dificultad que se espera en el nivel para calcular el fitness
    double[] dificultad_nivel;

    //Variable para guardar las ponderaciones a las que queremos establecer para calcular el fitnes (a que variables se les quiere dar mas importancia)
    double[] ponderaciones_nivel;

    //variables que van a guardar el area de seguridad de la puerta con los tesoros y con los monstruos
    double[] area_puerta_M;
    double[] area_puerta_T;

    //Variable que va a almacenar el recorrido entre una puerta y 
    //otra y va a almacenar que contienen las celdas del camino
    //(si hay tesoros o mosntruos para calcular fitness)
    private ArrayList<String[]> recorrido = new ArrayList<String[]>();

    //variable que va a guardar el recorrido de la puerta de entrada con cada una de las demas puertas
    private ArrayList<ArrayList<String[]>> recorridos;

    //Variable que va a guardar el resultado del fitness desglosado
    double[] resultados;
    double[] distancias_esperadas;
    double[] distancias_reales;

    //Numero de monstruos que hay en el camino
    private int num_monstruos_recorrido;
    //Numero de tesoros que hay en el camino
    private int num_tesoros_recorrido;

    private String puertaEntradaEscogida;
    private Celda puertaEntrada;

    public double getFitness() {
        return this.fitness;
    }

    /**
     * Constructor para inicializar.
     */
    public Dungeon() {
        super();
    }

    /**
     * Constructor de Dungeon
     *
     * @throws CloneNotSupportedException
     */
    public Dungeon(int _f, int _c, int numero_monstruos_, int numero_tesoros_, ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas, int numero_puertas_, int porcentaje_, int porcentaje_paredes_, int tipo_celdas_, double[] _dificultad_nivel, double[] _ponderaciones_nivel) throws CloneNotSupportedException {
        //Se igualan las dimensiones del dungeon
        f = _f;
        c = _c;

        //se guarda el tipo de celdas que va a haber para saber el tamano del genotipo, tanto del individuo como de cada celda
        tipo_celdas = tipo_celdas_;

        porcentaje = porcentaje_;
        porcentaje_paredes = porcentaje_paredes_;

        celdas_Paredes = (porcentaje_paredes * (f * c)) / 100;
        celdas_Vacias = (porcentaje * (f * c)) / 100;

        dificultad_nivel = _dificultad_nivel;
        ponderaciones_nivel = _ponderaciones_nivel;

        //Inicializamos el dungeon para crear primero las dimensiones que va a tener el mapa
        inicializarDungeon();

        //ResetearDungeon();
        /*LOG
		System.out.println("Termino inicializar dungeon");
         */
        //SE ANADEN LOS OBJETOS AL MAPA***************************
        revisar_genotipo();

        numero_puertas = numero_puertas_;

        this.pos_puertas = pos_puertas;
        this.t_puertas = t_puertas;

        //Se anaden las puertas al mapa
        //anadir_puertas_posicion(pos_puertas, t_puertas, numero_puertas);
        this.posicion_puertas = new ArrayList<Celda>();

        anadir_puertas(t_puertas, numero_puertas_);

        //Se comprueban los tesoros del mapa
        this.numero_tesoros = 0;
        this.numero_tesoros = comprobar_tesoros();

        this.numero_monstruos = 0;
        this.numero_monstruos = comprobar_monstruos();

        int num_tes_ant = this.numero_tesoros;
        int num_mons_ant = this.numero_monstruos;
        comprobar_tesoros();
        comprobar_monstruos();
        if (this.numero_monstruos != num_mons_ant || this.numero_tesoros != num_tes_ant) {
            System.out.println("ERROR");
            pintar();
            System.out.println("Continue");
        }

        ResetearDungeonCamino();
        //Se establecen los vecinos por cada celda

        int posicionStartX = -1;
        int posicionStartY = -1;

        for (int i = 0; i < this.posicion_puertas.size(); i++) {
            if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_N) {
                if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_N.name().equals("ENTRADA_SALIDA")) {
                    posicionStartX = this.posicion_puertas.get(i).fila;
                    posicionStartY = this.posicion_puertas.get(i).columna;
                }
            } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_S) {
                if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_S.name().equals("ENTRADA_SALIDA")) {
                    posicionStartX = this.posicion_puertas.get(i).fila;
                    posicionStartY = this.posicion_puertas.get(i).columna;
                }
            } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_E) {
                if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_E.name().equals("ENTRADA_SALIDA")) {
                    posicionStartX = this.posicion_puertas.get(i).fila;
                    posicionStartY = this.posicion_puertas.get(i).columna;
                }
            } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_O) {
                if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_O.name().equals("ENTRADA_SALIDA")) {
                    posicionStartX = this.posicion_puertas.get(i).fila;
                    posicionStartY = this.posicion_puertas.get(i).columna;
                }
            }
        }

        generateDungeon(posicionStartX, posicionStartY);

        //SE CALCULA EL FITNESS DEL INDIVIDUO**********************
        calcularfitness(numero_puertas);

    }

    /**
     * Funcion que crea un dungeon de las dimensiones f c
     */
    public void inicializarDungeon() {

        //creamos el dungeon con las dimensiones que nos han pasado para saber las dimensiones totales
        this.dungeon = new Celda[f][c];

        //Se inicializa el genotipo 
        int tamano_genotipo = f * c * tipo_celdas;
        genotipo = new int[tamano_genotipo];

        //Variables para saber en que celda me encuentro y para poder calcular asi la posicion en la que me encuentro del genotipo
        int contador_celda = 0;
        int posicion = 0;

        //Variables random para crear el genotipo de una celda
        int random_porcentaje = 0;

        //Variables para saber cuantas celdas tienen que haber de tipo pared y de tipo vacia para que este mas o menos en el porcentaje que 
        //se espera
        int numero_celdas_Paredes = (porcentaje_paredes * (f * c)) / 100;

        int numero_celdas_Vacias = (porcentaje * (f * c)) / 100;

        //Variable que va a guardar el genotipo de la celda en la que nos encontramos
        int[] genotipo_celda = null;

        //reseteo la variable de celdas pared para saber cuantas se han colocado al final al igual que de las celdas en blanco
        celdas_Paredes = 0;
        celdas_Vacias = 0;

        //Inicializamos el dungeon (sin pintarlo).
        for (int i = 0; i < this.dungeon.length; i++) {
            for (int j = 0; j < this.dungeon[i].length; j++) {
                //Genero un numero aleatorio de 0 a 100 
                random_porcentaje = new Random().nextInt(((100 - 0) + 1) + 0);

                //Variable que va a guardar el genotipo de la celda en la que nos encontramos
                genotipo_celda = new int[tipo_celdas];

                //Para cada bit del genotipo de cada celda genero un numero aleatorio (0 o 1)
                for (int n_tipo_celdas = 0; n_tipo_celdas < tipo_celdas; n_tipo_celdas++) {
                    //posicion del array del genotipo donde se tiene que guardar cada bit
                    posicion = (contador_celda * tipo_celdas) + n_tipo_celdas;

                    //Si el numero random creado esta dentro de 0 y porcentaje y no se han puesto todas las celdas vacias, se crea celda vacia
                    if (random_porcentaje >= 0 && random_porcentaje < porcentaje && numero_celdas_Vacias != 0) //porcentaje
                    {
                        //Si me encuentro en el ultimo bit elimino una posible celda que tenia que estar vacia
                        if (n_tipo_celdas == 2) {
                            //Resto una celda de las vacias que deberia de colocar como maximo
                            numero_celdas_Vacias--;

                            //sumo una a las celdas vacias que se han colocado
                            celdas_Vacias++;

                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 0;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];

                        } //Si no es el ultimo bit entonces se pone a 0 la posicion 0 y 1
                        else {
                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 0;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];
                        }
                    } //Si el numero random creado esta entre porcentaje y el porcentaje de paredes + porcentaje y puedo colocar mas paredes, se crea una pared
                    else if (random_porcentaje < (porcentaje + porcentaje_paredes) && random_porcentaje >= porcentaje && numero_celdas_Paredes != 0) {
                        //Si me encuentro en el ultimo bit pongo el genotipo igual a 1 para que sea 111
                        if (n_tipo_celdas == 2) {
                            //Resto una celda de las paredes que deberia de colocar como maximo
                            numero_celdas_Paredes--;

                            //sumo una a las celdas pared que se han colocado
                            celdas_Paredes++;

                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 1;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];
                        } //Si no es el ultimo bit entonces se pone a 1 al ultimo bit
                        else {
                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 1;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];
                        }

                    } //Si el numero random creado esta entre (porcentaje + porcentaje_pared) y 100% o ya he colocado todas las celdas vacias o paredes posibles, entonces hacemos que sea de tipo objeto 
                    else {

                        //Si me encuentro en el ultimo bit del genotipo y es igual a vacio la transcripcion, entonces cambio el ultimo bit para que no sea celda vacia
                        if ((genotipo_celda[0] == 0 && genotipo_celda[1] == 0) && (n_tipo_celdas == 2)) {

                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 1;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[2];

                        } //Si el genotipo es todo 1 generamos el ultimo bit a 0 para que no sea de tipo pared
                        else if ((genotipo_celda[0] == 1 && genotipo_celda[1] == 1) && (n_tipo_celdas == 2)) {

                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = 0;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];

                        } //Genero un bit aleatorio ya que no ha cumplido ninguna de las reglas anteriores
                        else {
                            //Se crea un random entre el 0 y el 1 (ya que devuelve un entero solo van a ser esos dos)
                            int random_genotipo = new Random().nextInt(((1 - 0) + 1) + 0);

                            //guardo el bit creado en el array con todos los bits del individuo
                            genotipo[posicion] = random_genotipo;

                            //Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
                            genotipo_celda[n_tipo_celdas] = genotipo[posicion];
                        }
                    }

                }

                Celda celdaCreada = null;
                celdaCreada = new Celda(i, j, f - 1, c - 1, false, genotipo_celda);

                //si la celda es de tipo pared, ponemos el booleano a true
                if (celdaCreada.genotipo_celda[0] == 1 && celdaCreada.genotipo_celda[1] == 1 && celdaCreada.genotipo_celda[2] == 1) {
                    celdaCreada.pared = true;
                } else {
                    celdaCreada.pared = false;
                }

                //creamos una celda con las posiciones que recibimos y le ponemos que es falsa la visita, tambien se le envía el genotipo que va a tener la celda
                try {
                    this.dungeon[i][j] = (Celda) celdaCreada.clone();
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.dungeon[i][j].monstruo = false;
                this.dungeon[i][j].tesoro = false;

                //Incremento el contador de la celda
                contador_celda++;
            }
        }
    }

    /**
     * Funcion que revisa si el genotipo de la celda coincide con el genotipo
     * global del mapa (almacena en la celda el genotipo correspondiente del
     * genotipo global)
     */
    public void revisar_genotipo() {

        //Variables para saber en que celda me encuentro y para poder calcular asi la posicion en la que me encuentro del genotipo
        int contador_celda = 0;
        int posicion = 0;

        //reseteo la variable de celdas pared para saber cuantas se han colocado al final al igual que de las celdas en blanco
        this.celdas_Paredes = 0;
        this.celdas_Vacias = 0;

        for (int i = 0; i < this.dungeon.length; i++) {
            for (int j = 0; j < this.dungeon[i].length; j++) {
                //Para cada bit del genotipo de cada celda
                for (int n_tipo_celdas = 0; n_tipo_celdas < tipo_celdas; n_tipo_celdas++) {
                    //posicion del array del genotipo donde se tiene que guardar cada bit
                    posicion = (contador_celda * tipo_celdas) + n_tipo_celdas;

                    this.dungeon[i][j].genotipo_celda[n_tipo_celdas] = genotipo[posicion];
                    this.dungeon[i][j].pared = false;
                    this.dungeon[i][j].monstruo = false;
                    this.dungeon[i][j].tesoro = false;
                }

                if (this.dungeon[i][j].genotipo_celda[0] == 0 && this.dungeon[i][j].genotipo_celda[1] == 0 && this.dungeon[i][j].genotipo_celda[2] == 0) {
                    this.celdas_Vacias++;

                } else if (this.dungeon[i][j].genotipo_celda[0] == 1 && this.dungeon[i][j].genotipo_celda[1] == 1 && this.dungeon[i][j].genotipo_celda[2] == 1) {
                    this.celdas_Paredes++;

                    //si la celda es de tipo pared, ponemos el booleano a true
                    this.dungeon[i][j].pared = true;

                }

                //Incremento el contador de la celda
                contador_celda++;
            }

        }
    }

    /**
     * Funcion que recorre todo el dungeon para ir poniendo las celdas vecinas
     * en cada celda del dungeon
     *
     * @param _x fila de la celda no visitada
     * @param _y columna de la celda no visitada
     */
    public void generateDungeon(int _x, int _y) {

        Celda inicio = this.dungeon[_x][_y]; //guardamos las coordenadas de la celda en la que nos encontramos

        inicio.visitada = true;//marcamos la celda como visitada

        //Guardamos todas las celdas vecinas de la celda actual recorriendo la lista
        ArrayList<Celda> vecinos = get_Vecinos(inicio);

        //Recorremos la lista de vecinos para comprobar cuales han sido visitados y cuales no y el primero que no esta
        //visitado lo visitamos
        Iterator<Celda> it = vecinos.iterator();
        //Mientras haya un vecino siguiente en la lista por el cual no hayamos pasado seguimos dentro del while
        while (it.hasNext()) {
            //Cojo la primera vecina de la lista de vecinas.
            Celda currentVecino = it.next();//guardamos la pared del vecino que tenemos como siguiente

            //Guardo una referencia a la celda vecina.
            int[] siguientePosicion = {(currentVecino.fila), (currentVecino.columna)};
            Celda neighbour = this.dungeon[siguientePosicion[0]][siguientePosicion[1]];

            //si el vecino ha sido visitado o es de tipo muro continuo, sino lo marco como celda visitada en la siguiente iteracion
            if (neighbour.visitada) //|| (neighbour.genotipo_celda[0] == 1 && neighbour.genotipo_celda[1] == 1 && neighbour.genotipo_celda[2] == 1))
            {
                //Si ha sido visitada continuo sin hacer nada, pues es una celda que ya ha sido abierta por algœn lado
                continue;
            } else {
                //Marco el vecino como visitado, puesto que va a ser nuestra siguiente posicion
                generateDungeon(neighbour.fila, neighbour.columna);
            }

        }

    }

    /**
     * Funcion que se encarga de guardar en el arraylist las celdas vecinas de
     * la celda que recibe
     *
     * @param celda Celda que queremos ver sus vecinos
     * @return se devuelve el arraylist que guarda la celda con los
     * correspondientes vecinos
     */
    public ArrayList<Celda> get_Vecinos(Celda celda) {
        celda.Vecinos = new ArrayList<Celda>();

        if (!this.dungeon[celda.fila][celda.columna].pared) {
            //Si soy esquina superior izquierda
            if (celda.fila == 0 && celda.columna == 0) {
                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); // Vecino  por el Este
                }

                if (!this.dungeon[celda.fila + 1][0].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][0]); //Vecino por el Sur
                }

            } //Si soy esquina superior derecha
            else if (celda.columna == celda.max_columna && celda.fila == 0) {
                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

                if (!this.dungeon[celda.fila + 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
                }

            } //Si soy esquina inferior derecha
            else if (celda.fila == celda.max_fila && celda.columna == celda.max_columna) {
                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

            } //si soy esquina inferior izquierda
            else if (celda.fila == celda.max_fila && celda.columna == 0) {

                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
                }

            } //Si soy lado derecho
            else if (celda.columna == celda.max_columna && (celda.fila > 0 && celda.fila < celda.max_fila)) {

                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

                if (!this.dungeon[celda.fila + 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
                }

                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

            } //Si soy lado izquierdo
            else if (celda.columna == 0 && (celda.fila > 0 && celda.fila < celda.max_fila)) {
                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

                if (!this.dungeon[celda.fila + 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
                }

                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
                }

            } //Si soy lado norte
            else if (celda.fila == 0 && (celda.columna > 0 && celda.columna < celda.max_columna)) {

                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
                }

                if (!this.dungeon[celda.fila + 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
                }
            } //Si soy lado sur
            else if (celda.fila == celda.max_fila && (celda.columna > 0 && celda.columna < celda.max_columna)) {
                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
                }

                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

            } //Si estoy por el medio
            else if ((celda.fila > 0 && celda.fila < celda.max_fila) && (celda.columna > 0 && celda.columna < celda.max_columna)) {
                if (!this.dungeon[celda.fila][celda.columna + 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
                }

                if (!this.dungeon[celda.fila][celda.columna - 1].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
                }

                if (!this.dungeon[celda.fila - 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
                }

                if (!this.dungeon[celda.fila + 1][celda.columna].pared) {
                    celda.Vecinos.add(this.dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
                }
            }
        }

        return celda.Vecinos;
    }

    /**
     * Funcion que anade puertas al mapa
     *
     * @param f filas
     * @param c columnas
     * @param numero_puertas
     */
    @SuppressWarnings("unused")
    public void anadir_puertas(ArrayList<Celda.Tipo_puertas> t_puertas, int numero_puertas) {

        //Se inicializan las variables para saber si ya hay puerta o no en ese lado por cada individuo
        this.hay_puerta_N = false;
        this.hay_puerta_S = false;
        this.hay_puerta_E = false;
        this.hay_puerta_O = false;

        //Variables para comprobar si no hay puertas de entrada_salida y salida establecerlas
        boolean p_salida = false;
        boolean p_entrada_salida = false;

        //Variables para generar la posicion random donde voy a colocar la puerta
        int random_x = 0;
        int random_y = 0;

        //variable para contar cuantas veces hemos intentado poner la puerta en una celda que estuviera sin pared
        int iteraciones = 0;

        this.posicion_puertas = new ArrayList<Celda>();

        //Se anaden las x puertas a cada mapa en posiciones random
        for (int puertas = 0; puertas < numero_puertas; puertas++) {

            random_x = 0;
            random_y = 0;

            //Si no hay puerta norte
            if (!this.hay_puerta_N) {

                random_x = 0;

                iteraciones = 0;

                random_y = (int) (Math.random() * (c - min) + min);

                while (this.dungeon[random_x][random_y].puerta) {
                    random_y = (int) (Math.random() * (c - min) + min);
                }

                //Si tenemos el tipo de puerta que es lo establecemos
                if (t_puertas != null && t_puertas.size() != 0) {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.dungeon[random_x][random_y].puerta_N = true;
                    this.dungeon[random_x][random_y].tipo_puerta_N = t_puertas.get(puertas);
                    this.hay_puerta_N = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);

                } else {

                    this.dungeon[random_x][random_y].puerta = true;
                    this.hay_puerta_N = true;
                    this.dungeon[random_x][random_y].puerta_N = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);

                    //Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
                    int random_tipo_puerta = (int) (Math.random() * (0 - (2)) + (2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0

                    if ((numero_puertas - puertas) > 1) {
                        if (random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    } else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
                    {
                        if (!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si hay entrada_salida ponemos una salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    }

                }
            } //Si no hay puerta sur
            else if (!this.hay_puerta_S) {
                random_x = f - 1;

                iteraciones = 0;

                random_y = (int) (Math.random() * (c - min) + min);

                while (this.dungeon[random_x][random_y].puerta) {
                    random_y = (int) (Math.random() * (c - min) + min);
                }

                //Si tenemos el tipo de puerta que es lo establecemos
                if (t_puertas != null && t_puertas.size() != 0) {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.dungeon[random_x][random_y].puerta_S = true;
                    this.dungeon[random_x][random_y].tipo_puerta_S = t_puertas.get(puertas);
                    this.hay_puerta_S = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);
                } else {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.hay_puerta_S = true;
                    this.dungeon[random_x][random_y].puerta_S = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);

                    //Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
                    int random_tipo_puerta = (int) (Math.random() * (0 - (2)) + (2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0

                    if ((numero_puertas - puertas) > 1) {
                        if (random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    } else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
                    {
                        if (!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si hay entrada_salida ponemos una salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    }
                }

            } //Si no hay puerta este
            else if (!this.hay_puerta_E) {

                random_y = c - 1;

                iteraciones = 0;

                random_x = (int) (Math.random() * (f - min) + min);

                while (this.dungeon[random_x][random_y].puerta) {
                    random_x = (int) (Math.random() * (f - min) + min);
                }

                //Si tenemos el tipo de puerta que es lo establecemos
                if (t_puertas != null && t_puertas.size() != 0) {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.dungeon[random_x][random_y].puerta_E = true;
                    this.dungeon[random_x][random_y].tipo_puerta_E = t_puertas.get(puertas);
                    this.hay_puerta_E = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
                } else {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.hay_puerta_E = true;
                    this.dungeon[random_x][random_y].puerta_E = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);

                    //Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
                    int random_tipo_puerta = (int) (Math.random() * (0 - (2)) + (2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0

                    if ((numero_puertas - puertas) > 1) {
                        if (random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    } else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
                    {
                        if (!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si hay entrada_salida ponemos una salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    }
                }

            } //Si no hay puerta oeste
            else if (!this.hay_puerta_O) {

                random_y = 0;

                iteraciones = 0;

                random_x = (int) (Math.random() * (f - min) + min);

                while (this.dungeon[random_x][random_y].puerta) {
                    random_x = (int) (Math.random() * (f - min) + min);
                }

                //Si tenemos el tipo de puerta que es lo establecemos
                if (t_puertas != null && t_puertas.size() != 0) {
                    this.dungeon[random_x][random_y].puerta = true;
                    this.dungeon[random_x][random_y].puerta_O = true;
                    this.dungeon[random_x][random_y].tipo_puerta_O = t_puertas.get(puertas);
                    this.hay_puerta_O = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);
                } else {
                    this.dungeon[random_x][random_y].puerta = true;
                    hay_puerta_O = true;
                    this.dungeon[random_x][random_y].puerta_O = true;

                    //se guarda la posicion de la puerta en el mapa
                    int[] posicion = {random_x, random_y};
                    this.posicion_puertas.add(this.dungeon[posicion[0]][posicion[1]]);

                    //Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
                    int random_tipo_puerta = (int) (Math.random() * (0 - (2)) + (2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0

                    if ((numero_puertas - puertas) > 1) {
                        if (random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
                        {
                            dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }

                    } else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
                    {
                        if (!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.ENTRADA_SALIDA;
                            p_entrada_salida = true;
                        } else //Si hay entrada_salida ponemos una salida
                        {
                            this.dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.SALIDA;
                            p_salida = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Funcion que comprueba los tesoros del mapa
     *
     * @param f filas
     * @param c columnas
     * @param numero_tesoros
     */
    public int comprobar_tesoros() throws CloneNotSupportedException {
        int numero_tesoros = 0;
        this.posicion_tesoros = null;
        this.posicion_tesoros = new ArrayList<Celda>();
        //Se comprueban los tesoros que se han anadido en el mapa
        for (int fila = 0; fila < f; fila++) {

            for (int columna = 0; columna < c; columna++) {

                //Hay una moneda
                if (this.dungeon[fila][columna].genotipo_celda[0] == 0 && this.dungeon[fila][columna].genotipo_celda[1] == 1 && this.dungeon[fila][columna].genotipo_celda[2] == 1) {
                    this.dungeon[fila][columna].tesoro = true;
                    this.posicion_tesoros.add((Celda) dungeon[fila][columna].clone());

                    numero_tesoros = this.posicion_tesoros.size();

                } //Hay una llave
                else if (this.dungeon[fila][columna].genotipo_celda[0] == 1 && this.dungeon[fila][columna].genotipo_celda[1] == 0 && this.dungeon[fila][columna].genotipo_celda[2] == 1) {
                    this.dungeon[fila][columna].tesoro = true;
                    this.posicion_tesoros.add((Celda) this.dungeon[fila][columna].clone());

                    numero_tesoros = this.posicion_tesoros.size();

                } //Hay una piedra preciosa
                else if (this.dungeon[fila][columna].genotipo_celda[0] == 1 && this.dungeon[fila][columna].genotipo_celda[1] == 1 && this.dungeon[fila][columna].genotipo_celda[2] == 0) {
                    this.dungeon[fila][columna].tesoro = true;
                    this.posicion_tesoros.add((Celda) this.dungeon[fila][columna].clone());

                    numero_tesoros = this.posicion_tesoros.size();

                }

            }

        }

        return numero_tesoros;
    }

    /**
     * Funcion que comprueba los tesoros del mapa
     *
     * @param f filas
     * @param c columnas
     * @param numero_tesoros
     */
    public int comprobar_monstruos() throws CloneNotSupportedException {
        int numero_monstruos = 0;

        this.posicion_monstruos = null;
        this.posicion_monstruos = new ArrayList<Celda>();

        //Se comprueban los monstruos que se han anadido en el mapa
        for (int fila = 0; fila < f; fila++) {

            for (int columna = 0; columna < c; columna++) {

                //Hay un gigante
                if (this.dungeon[fila][columna].genotipo_celda[0] == 0 && this.dungeon[fila][columna].genotipo_celda[1] == 0 && this.dungeon[fila][columna].genotipo_celda[2] == 1) {
                    this.dungeon[fila][columna].monstruo = true;
                    this.posicion_monstruos.add((Celda) this.dungeon[fila][columna].clone());

                    numero_monstruos = this.posicion_monstruos.size();

                } //Hay un murcielago
                else if (this.dungeon[fila][columna].genotipo_celda[0] == 0 && this.dungeon[fila][columna].genotipo_celda[1] == 1 && this.dungeon[fila][columna].genotipo_celda[2] == 0) {
                    this.dungeon[fila][columna].monstruo = true;
                    this.posicion_monstruos.add((Celda) this.dungeon[fila][columna].clone());

                    numero_monstruos = this.posicion_monstruos.size();

                } //Hay un soldado
                else if (this.dungeon[fila][columna].genotipo_celda[0] == 1 && this.dungeon[fila][columna].genotipo_celda[1] == 0 && this.dungeon[fila][columna].genotipo_celda[2] == 0) {
                    this.dungeon[fila][columna].monstruo = true;
                    this.posicion_monstruos.add((Celda) this.dungeon[fila][columna].clone());

                    numero_monstruos = this.posicion_monstruos.size();

                }
            }

        }

        return numero_monstruos;
    }

    /**
     * Funcion que realiza la busqueda del camino optimo entre la celda origen y
     * la meta
     *
     * @param x_inicio
     * @param y_inicio
     * @param x_final
     * @param y_final
     * @throws CloneNotSupportedException
     */
    public void llegada_optima(int x_inicio, int y_inicio, int x_final,
            int y_final) throws CloneNotSupportedException {

        //se resetea el camino de todo el mapa
        this.ResetearDungeonCamino();

        //Se inicializa la distancia
        this.distancia = -1;

        //Si la celda de inicio es de tipo pared o la de meta entonces no hay camino
        if ((this.dungeon[x_inicio][y_inicio].pared)
                || (this.dungeon[x_final][y_final].pared)) {
            this.distancia = -1;
            // ResetearDungeonCamino();
        } //Si el destino es donde me encuentro entonces no hay que buscar ningun camino ya que nos encontramos en la meta
        else if (x_inicio == x_final && y_inicio == y_final) {
            //la distancia es 0 
            this.distancia = 0;

            //ResetearDungeonCamino();
        } //Sino puede que haya un camino	
        else {

            Celda celdaActual = new Celda();

            ArrayList<Celda> listaCeldasAbiertas = new ArrayList<Celda>();
            ArrayList<Celda> listaCeldasCerradas = new ArrayList<Celda>();

            // Se anade la celda de inicio a la lista de celdas abiertas
            listaCeldasAbiertas.add((Celda) this.dungeon[x_inicio][y_inicio]);

            // Mientras la lista de celdas abiertas no este vacia seguimos
            while (listaCeldasAbiertas.size() != 0) {
                // se obtiene la celda con menor F
                celdaActual = celdaMenorF(listaCeldasAbiertas);

                // se anade la celda actual a la lista de celdas cerradas
                listaCeldasCerradas.add(celdaActual);
                for (int i = 0; i < listaCeldasAbiertas.size(); i++) {
                    if (listaCeldasAbiertas.get(i).fila == celdaActual.fila
                            && listaCeldasAbiertas.get(i).columna == celdaActual.columna) {
                        listaCeldasAbiertas.remove(i);
                    }
                }
                // si la celda final es el destino paramos
                if (celdaActual.fila == x_final && celdaActual.columna == y_final) {

                    break;
                } else {
                    // obtenemos la lista de vecinos/adyacentes de la celda
                    // actual
                    ArrayList<Celda> vecinos = getlistaVecinos(celdaActual);

                    // se recorren los vecinos de la celda actual
                    for (Celda vecino : vecinos) {
                        // si la lista de celdas abiertas no contiene
                        // al vecino ni la lista de cerradas

                        int posicionAbiertas = -1;
                        int posicionCerradas = -1;

                        //Se recorre la lista de celdas abiertas para ver si existe la celda vecino
                        for (int i = 0; i < listaCeldasAbiertas.size(); i++) {
                            if (listaCeldasAbiertas.get(i).fila == vecino.fila
                                    && listaCeldasAbiertas.get(i).columna == vecino.columna) {
                                posicionAbiertas = i;
                            }
                        }

                        //Se recorre la lista de celdas cerradas para ver si existe la celda vecino
                        for (int i = 0; i < listaCeldasCerradas.size(); i++) {
                            if (listaCeldasCerradas.get(i).fila == vecino.fila
                                    && listaCeldasCerradas.get(i).columna == vecino.columna) {
                                posicionCerradas = i;
                            }
                        }

                        if (posicionAbiertas == -1 && posicionCerradas == -1) {
                            // Se establecen los costes F, G, H
                            vecino.setEcuacion(celdaActual,
                                    this.dungeon[x_inicio][y_inicio],
                                    this.dungeon[x_final][y_final]);

                            // Se guarda el padre del vecino
                            vecino.Posicion_precursor[0] = celdaActual.fila;
                            vecino.Posicion_precursor[1] = celdaActual.columna;

                            // Se anade a la lista de celdas abiertas la celda
                            // vecina/adyacente
                            listaCeldasAbiertas
                                    .add(vecino);
                        } // si la lista de celdas abiertas contiene la celda
                        // vecina,
                        // calculamos si la
                        // distancia es peor a la de ahora.
                        else if (posicionAbiertas > -1) {
                            // si el coste real es menor al establecido
                            // anteriormente,
                            // se establecen los costes
                            if (vecino.getG() < celdaActual.getG()) {
                                // se establecen los costes F, G, H
                                vecino.setEcuacion(
                                        celdaActual,
                                        this.dungeon[x_inicio][y_inicio],
                                        this.dungeon[x_final][y_final]);

                                // Hago que apunte al padre actual
                                vecino.Posicion_precursor[0] = celdaActual.fila;
                                vecino.Posicion_precursor[1] = celdaActual.columna;

                            }
                        }
                    }
                }
            }

            //si la celda destino tiene un precursor es que hay camino, sino la distancia es -1
            if (this.dungeon[x_final][y_final].Posicion_precursor[1] != -1) {
                //Recorremos el camino optimo empezando desde la celda final
                RecorrerCamino(this.dungeon[x_final][y_final], this.dungeon[x_inicio][y_inicio]);
            } else {
                this.distancia = -1;
            }

        }
    }

    /**
     *
     * @param lista
     * @return
     * @throws CloneNotSupportedException
     */
    public Celda celdaMenorF(ArrayList<Celda> lista) throws CloneNotSupportedException {

        Celda celdaMenor = new Celda();

        //se recorre la lista de celdas para encontrar la celda con menor F
        for (int i = 0; i < lista.size(); i++) {
            if (i == 0) {
                celdaMenor = (Celda) lista.get(0).clone();
            } else if (lista.size() > 1) {
                if (lista.get(i).getF() < celdaMenor.getF()) {
                    celdaMenor = (Celda) lista.get(i).clone();
                }
            }
        }

        return (Celda) celdaMenor.clone();
    }

    /**
     * Funcion calcula la distancia entre las puertas y los tesoros guardando un
     * arraylist con las distancias
     *
     * @param numero_puertas numero de puertas que hay en el mapa
     * @param numero_tesoros numero de tesoros que hay en el mapa
     * @throws CloneNotSupportedException
     */
    public void calcular_distancias_PT(int numero_puertas, int numero_tesoros) throws CloneNotSupportedException {

        //Variable para guardar la distancia entre una puerta y un tesoro
        int distancia_ = 0;
        this.distancia = -1;

        //Variable para guardar las caracteristicas de la celda donde se encuentra el T mas cercano a la P
        Celda posicion_T_minima = null;

        //Se calculan las distancias entre cada puerta con cada tesoro
        for (int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
        {

            //Inicializo la variable que va a guardar las distancias con todos los tesoros
            this.posicion_puertas.get(contador_puertas).tam_dist_T = new ArrayList<Integer>();

            //Variable para guardar la distancia del tesoro mas cercano a la puerta (se inicializa al mayor numero de movimientos que podria hacer que es f x c )
            int distancia_minima = f * c;

            //Si no hay tesoros salgo del bucle
            if (numero_tesoros == 0) {
                /*//LOG
				System.out.println("NO HAY TESOROS");
                 */
                break;
            } else {
                //Para cada tesoro con respecto a una puerta se guarda su distancia
                for (int contador_tesoros = 0; contador_tesoros < numero_tesoros; contador_tesoros++) {

                    //Se calcula la distancia de cada puerta con cada tesoro
                    llegada_optima(this.posicion_puertas.get(contador_puertas).fila, this.posicion_puertas.get(contador_puertas).columna, this.posicion_tesoros.get(contador_tesoros).fila, this.posicion_tesoros.get(contador_tesoros).columna);

                    //si la puerta es de tipo entrada salida entonces guardo su distancia para luego calcular el fitness con esa distancia
                    if (this.posicion_puertas.get(contador_puertas).tipo_puerta_N.equals(Tipo_puertas.ENTRADA_SALIDA)
                            || posicion_puertas.get(contador_puertas).tipo_puerta_S.equals(Tipo_puertas.ENTRADA_SALIDA)
                            || posicion_puertas.get(contador_puertas).tipo_puerta_E.equals(Tipo_puertas.ENTRADA_SALIDA)
                            || this.posicion_puertas.get(contador_puertas).tipo_puerta_O.equals(Tipo_puertas.ENTRADA_SALIDA)) {
                        posicion_tesoros.get(contador_tesoros).distancia_P_cercana = this.distancia;
                    }

                    //Si no hay camino entre un tesoro y una puerta la habitacion no es valida
                    if (distancia == -1) {
                        this.dungeon_valido = false;
                    }

                    distancia_ = this.distancia;

                    //se anade una posicion con valor distancia_ por cada tesoro que haya en el mapa para guardar su distancia con respecto a la puerta i
                    this.posicion_puertas.get(contador_puertas).tam_dist_T.add(distancia_);

                    //Si la distancia calculada es inferior a la distancia minima almacenada se guardan los datos
                    if (distancia_ < distancia_minima && this.distancia != -1) //&& distancia_ >= 0) //>= 0 si se quiere que en la celda donde se encuentra la puerta pueda haber un tesoro
                    {
                        //Se iguala la distancia minima con la calculada
                        distancia_minima = distancia_;

                        //Se guarda la posicion del tesoro
                        posicion_T_minima = this.posicion_tesoros.get(contador_tesoros);
                    }

                    //si me encuentro en la ultima vuelta y todos los tesoros tienen una distancia negativa, entonces guardo como la distancia minima -1 
                    //(se puede deber a que la puerta este incomunicada o que sea de tipo pared esa celda)
                    if (this.distancia == -1 && (contador_tesoros == (numero_tesoros - 1)) && distancia_minima == f * c) {
                        //Se iguala la distancia minima con la calculada
                        distancia_minima = -1;

                        //Se guarda la posicion de la celda donde esta la puerta para tener una posicion
                        posicion_T_minima = this.posicion_puertas.get(contador_puertas);

                    }

                    //Se resetea la distancia
                    distancia_ = 0;
                    this.distancia = -1;

                }

                //Se anaden las distancias de cada tesoro con respecto a la puerta i
                //puertas_distancias.addAll(tam_dist);
                //Guardo en un arraylist las posicion del tesoro mas cercano a la puerta
                posicion_puertas.get(contador_puertas).Posicion_T_cercano[0] = posicion_T_minima.fila;
                posicion_puertas.get(contador_puertas).Posicion_T_cercano[1] = posicion_T_minima.columna;

                //Si no se ha modificado la distancia minima porque no hay ningun objeto o camino posible entre la puerta y los objetos, decimos que la distancia minima es -1
                if (distancia_minima == (f * c)) {
                    distancia_minima = - 1;
                }

                //Voy colocando en cada posicion la correspondiente medida
                if (posicion_puertas.get(contador_puertas).puerta_N) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PT.add(0, distancia_minima);

                } else if (posicion_puertas.get(contador_puertas).puerta_S) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PT.add(1, distancia_minima);
                } else if (posicion_puertas.get(contador_puertas).puerta_E) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PT.add(2, distancia_minima);
                } else if (posicion_puertas.get(contador_puertas).puerta_O) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PT.add(3, distancia_minima);
                } else {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PT.add(distancia_minima);
                }

            }

        }

    }

    /**
     * Funcion calcula la distancia entre las puertas y los monstruos guardando
     * un arraylist con las distancias
     *
     * @param Poblacion
     * @throws CloneNotSupportedException
     */
    public void calcular_distancias_PM(int numero_puertas, int numero_monstruos) throws CloneNotSupportedException {

        //Variable para guardar la distancia entre una puerta y un tesoro
        int distancia_ = 0;
        distancia = -1;

        //Variable para guardar las caracteristicas de la celda donde se encuentra el M mas cercano a la P
        Celda posicion_M_minima = null;

        //Se calculan las distancias entre cada puerta con cada tesoro
        for (int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
        {

            //Inicializo la variable que va a guardar las distancias con todos los monstruos
            posicion_puertas.get(contador_puertas).tam_dist_M = new ArrayList<Integer>();

            //Variable para guardar la distancia del tesoro mas cercano a la puerta (se inicializa al mayor numero de movimientos que podria hacer que es f x c )
            int distancia_minima = f * c;

            //Si no hay monstruos salgo del bucle
            if (numero_monstruos == 0) {
                /*//LOG
				System.out.println("NO HAY MONSTRUOS");
                 */
                break;
            } else {
                //Para cada tesoro con respecto a una puerta se guarda su distancia
                for (int contador_monstruos = 0; contador_monstruos < numero_monstruos; contador_monstruos++) {

                    //Se calcula la distancia de cada puerta con cada tesoro
                    llegada_optima(posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna, this.posicion_monstruos.get(contador_monstruos).fila, this.posicion_monstruos.get(contador_monstruos).columna);

                    //Si no hay camino entre un monstruo y una puerta la habitacion no es valida
                    if (distancia == -1) {
                        this.dungeon_valido = false;
                    }

                    distancia_ = distancia;

                    //se anade una posicion con valor distancia_ por cada monstruo que haya en el mapa para guardar su distancia con respecto a la puerta i
                    posicion_puertas.get(contador_puertas).tam_dist_M.add(distancia_);

                    //Si la distancia calculada es inferior a la distancia minima almacenada se guardan los datos
                    if (distancia_ < distancia_minima && distancia != -1) //&& distancia_ >= 0) //>= 0 si se quiere que en la celda donde se encuentra la puerta pueda haber un tesoro
                    {
                        //Se iguala la distancia minima con la calculada
                        distancia_minima = distancia_;

                        //Se guarda la posicion del monstruo
                        posicion_M_minima = this.posicion_monstruos.get(contador_monstruos);
                    }

                    //si me encuentro en la ultima vuelta y todos los monstruos tienen una distancia negativa, entonces guardo como la distancia minima -1 
                    //(se puede deber a que la puerta este incomunicada o que sea de tipo pared esa celda)
                    if (distancia == -1 && (contador_monstruos == (numero_monstruos - 1)) && distancia_minima == f * c) {
                        //Se iguala la distancia minima con la calculada
                        distancia_minima = -1;

                        //Se guarda la posicion de la celda donde esta la puerta para tener una posicion
                        posicion_M_minima = posicion_puertas.get(contador_puertas);

                    }

                    //Se resetea la distancia
                    distancia_ = 0;
                    distancia = -1;

                }

                //Guardo en un arraylist las posicion del tesoro mas cercano a la puerta
                posicion_puertas.get(contador_puertas).Posicion_M_cercano[0] = posicion_M_minima.fila;
                posicion_puertas.get(contador_puertas).Posicion_M_cercano[1] = posicion_M_minima.columna;

                //Si no se ha modificado la distancia minima porque no hay ningun monstruo o camino posible entre la puerta y los monstruos, decimos que la distancia minima es -1
                /*if (distancia_minima == (f * c))
				{
					distancia_minima = - 1;
				}*/
                //Voy colocando en cada posicion la correspondiente medida
                if (posicion_puertas.get(contador_puertas).puerta_N) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PM.add(0, distancia_minima);

                } else if (posicion_puertas.get(contador_puertas).puerta_S) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PM.add(1, distancia_minima);
                } else if (posicion_puertas.get(contador_puertas).puerta_E) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PM.add(2, distancia_minima);
                } else if (posicion_puertas.get(contador_puertas).puerta_O) {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PM.add(3, distancia_minima);
                } else {
                    //Guardo en un arraylist las distancias minimas
                    distancia_min_PM.add(distancia_minima);
                }

            }//cierra el else

        }//cierra el for

    }//cierra la funcion

    /**
     * Funcion que calcula si hay camino entre las puertas, ademas de calcular
     * si hay tesoros y monstruos en ese camino
     *
     * @throws CloneNotSupportedException
     */
    public void calcular_camino_PP(int numero_puertas) throws CloneNotSupportedException {

        //booleano que mientras haya una distancia entre la puerta 0 y las demas es que hay camino, de lo contrario, es un dungeon sin 
        //conexion entre las puertas
        this.dungeon_valido = true;

        //Se comprueba que no hay ninguna puerta que sea de tipo pared, si es así, el dungeon no es válido.
        for (int i = 0; i < this.posicion_puertas.size(); i++) {
            if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].genotipo_celda[0] == 1
                    && this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].genotipo_celda[1] == 1
                    && this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].genotipo_celda[2] == 1) {
                this.dungeon_valido = false;
                break;
            }
        }

        if (this.dungeon_valido == true) {
            //Se resetea la variable distancia
            distancia = -1;
            this.recorridos = null;
            this.recorridos = new ArrayList<ArrayList<String[]>>();

            puertaEntrada = new Celda();
            for (int i = 0; i < this.posicion_puertas.size(); i++) {
                if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_N) {
                    if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_N.name().equals("ENTRADA_SALIDA")) {
                        puertaEntrada = this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna];
                        puertaEntradaEscogida = "puerta_N";
                    }
                } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_S) {
                    if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_S.name().equals("ENTRADA_SALIDA")) {
                        puertaEntrada = this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna];
                        puertaEntradaEscogida = "puerta_S";
                    }
                } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_E) {
                    if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_E.name().equals("ENTRADA_SALIDA")) {
                        puertaEntrada = this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna];
                        puertaEntradaEscogida = "puerta_E";
                    }
                } else if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].puerta_O) {
                    if (this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna].tipo_puerta_O.name().equals("ENTRADA_SALIDA")) {
                        puertaEntrada = this.dungeon[this.posicion_puertas.get(i).fila][this.posicion_puertas.get(i).columna];
                        puertaEntradaEscogida = "puerta_O";
                    }
                }
            }

            //Se calcula la distancia entre la puerta 0 con el resto de puertas
            for (int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
            {
                //Se resetea la distancia
                distancia = -1;

                //si el dungeon es valido se comprueba la distancia, en el momento en el que ya no haya camino entre dos puertas será invalido el camino
                if (this.dungeon_valido != false && (posicion_puertas.get(contador_puertas).fila != puertaEntrada.fila || posicion_puertas.get(contador_puertas).columna != puertaEntrada.columna)) {
                    ResetearDungeonCamino();
                    this.recorrido = new ArrayList<String[]>();

                    //Se calcula la distancia de la puerta 0 con las demas
                    llegada_optima(puertaEntrada.fila, puertaEntrada.columna, posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna);

                    //si la distancia es mayor a 0 o la celda de la puerta coincide con la que estamos calculando se anade tambien ese recorrido
                    if (distancia > 0 || ((puertaEntrada.fila == posicion_puertas.get(contador_puertas).fila) && (puertaEntrada.columna == posicion_puertas.get(contador_puertas).columna))) {
                        if (puertaEntrada.puerta_N && !posicion_puertas.get(contador_puertas).puerta_N) {
                            this.recorridos.add(this.recorrido);
                        } else if (puertaEntrada.puerta_S && !posicion_puertas.get(contador_puertas).puerta_S) {
                            this.recorridos.add(this.recorrido);
                        } else if (puertaEntrada.puerta_E && !posicion_puertas.get(contador_puertas).puerta_E) {
                            this.recorridos.add(this.recorrido);
                        } else if (puertaEntrada.puerta_O && !posicion_puertas.get(contador_puertas).puerta_O) {
                            this.recorridos.add(this.recorrido);
                        }

                    } //Si hay una distancia es que es un dungeon valido porque hay un camino
                    else if (distancia > 0) {
                        this.dungeon_valido = true;
                    } //si la distancia es -1 entonces es que no hay un camino PP y ya no es un dungeon_valido
                    else if (distancia == -1) {
                        //					pintar();
                        this.dungeon_valido = false;
                    }
                }
            }

            if (this.recorridos.size() == (this.numero_puertas - 1)) {
                this.dungeon_valido = true;
            } else if (this.recorridos.size() < (this.numero_puertas - 1)) {
                //			pintar();
                this.dungeon_valido = false;
            }
        }
    }

    /**
     * Funcion que calcula el fitness de los tesoros con respecto al monstruo
     * mas cercano
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public double[] calcular_fitness_tesoros() throws CloneNotSupportedException {

        //varibale que va a guardar el fitness de cada tesoro
        double[] fitness_tesoros = new double[numero_tesoros];

        //metrica de seguridad del tesoro con respecto a la puerta y al monstruo
        float st = 0;

        //variable que va a almacenar todos los st para luego quedarme con el menor
        float[] st_provisional = new float[numero_monstruos];

        //Por cada tesoro que hay en el mapa calculo su fitness
        for (int tesoro = 0; tesoro < numero_tesoros; tesoro++) {

            //inicializo el array
            st_provisional = new float[numero_monstruos];

            //por cada monstruo que hay en el mapa calculo las distancias entre ese monstruo y el tesoro i
            for (int monstruo = 0; monstruo < numero_monstruos; monstruo++) {
                //calculo cual es la distancia minima entre el tesoro y el resto de monstruos que hay en el mapa
                llegada_optima(this.posicion_tesoros.get(tesoro).fila, this.posicion_tesoros.get(tesoro).columna, this.posicion_monstruos.get(monstruo).fila, this.posicion_monstruos.get(monstruo).columna);

                //obtengo el st
                st = distancia; //st_resta / st_suma;

                //si el resultado obtenido es mayor o igual a 0 entonces guardamos el resultado, sino guardamos un numero alto debido a que esa distancia no se puede calcular
                if (st >= 0) {
                    //guardo el resultado en un array
                    st_provisional[monstruo] = st;

                } else {
                    //guardo el resultado en un array
                    st_provisional[monstruo] = (f * c) + 1;
                }

                //reseteo las variables
                st = 0;

            }

            //reseteo el st para guardar el valor minimo del st
            st = f * c;

            //obtengo el valor con menor resultado del array provisional
            for (float valor : st_provisional) {
                //si el valor es mayor o igual a 0 y menor que el dato que hemos guardado entonces almacenamos ese dato
                if (valor >= 0.0 && valor < st) {
                    st = valor;
                }
            }

            //guardo en la posicion correspondiente el fitness minimo para ese tesoro
            Celda celda_mod = (Celda) this.dungeon[this.posicion_tesoros.get(tesoro).fila][this.posicion_tesoros.get(tesoro).columna].clone();
            celda_mod.distancia_seguridad_M = st;
            this.dungeon[celda_mod.fila][celda_mod.columna] = (Celda) celda_mod.clone();
            this.posicion_tesoros.set(tesoro, (Celda) celda_mod.clone());
            fitness_tesoros[tesoro] = st;

        }

        //devuelvo el array con los fitness calculados de cada tesoro
        return fitness_tesoros;
    }

    /**
     * Funcion que se encarga de calcular el fitness de las puertas, para ello
     * lo que hace es ver si tiene objetos cerca (se hace con busqueda en
     * anchura)
     *
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    public double[] calcular_fitness_puertas() throws CloneNotSupportedException {

        //array que vamos a pasar con los dos fitness de las puertas, el de los monstruos y el de los tesoros por cada puerta
        double[] fitness_puertas_total = new double[2 * this.posicion_puertas.size()];

        //variables que van a guardar el area de cada elemento con respecto a la puerta de entrada para luego calcular el fitness
        this.area_puerta_M = new double[this.posicion_monstruos.size()];
        this.area_puerta_T = new double[this.posicion_tesoros.size()];

        //variable que va a guardar el numero de las celdas seguras
        double area = 0.0;

        //variables para calcular el numerador y el denominador del fitness
        double numerador = 0.0;
        double denominador = 0.0;
        @SuppressWarnings("unused")
        double division = 0.0;

        //area maximo que va a haber entre la puerta y el monstruo/tesoro
        @SuppressWarnings("unused")
        double area_max = 0.0;

        //int contador_puertas = 0; //variable para saber en que posicion guardar el fitness de cada puerta
        if (this.posicion_monstruos.size() > 1) {
            int distancia_PM = -1;
            int distancia_PM_anterior = -1;

            ArrayList<Celda> posicion_monstruos_ordenados = this.ordenarCeldas(this.posicion_monstruos);
            //se ordenan los recorridos
            this.posicion_monstruos = new ArrayList<Celda>();
            this.posicion_monstruos = (ArrayList<Celda>) posicion_monstruos_ordenados.clone();
            this.area_puerta_M = new double[this.posicion_monstruos.size()];
            for (int i = 0; i < this.area_puerta_M.length; i++) {
                this.area_puerta_M[i] = -1.0;
            }

        } else if (this.posicion_monstruos.size() == 1) {

            this.area_puerta_M = new double[this.posicion_monstruos.size()];
            for (int i = 0; i < this.area_puerta_M.length; i++) {
                this.area_puerta_M[i] = -1.0;
            }

        }

        if (this.posicion_tesoros.size() > 1) {
            int distancia_PT = -1;
            int distancia_PT_anterior = -1;
            ArrayList<Celda> posicion_tesoros_ordenados = this.ordenarCeldas(this.posicion_tesoros);

            //se ordenan los recorridos
            this.posicion_tesoros = new ArrayList<Celda>();
            this.posicion_tesoros = (ArrayList<Celda>) posicion_tesoros_ordenados.clone();
            this.area_puerta_T = new double[this.posicion_tesoros.size()];
            for (int i = 0; i < this.area_puerta_T.length; i++) {
                this.area_puerta_T[i] = -1.0;
            }

        } else if (this.posicion_tesoros.size() == 1) {
            this.area_puerta_T = new double[this.posicion_tesoros.size()];
            for (int i = 0; i < this.area_puerta_T.length; i++) {
                this.area_puerta_T[i] = -1.0;
            }
        }

        //Se realiza este bucle 2 veces para poner el fitness de los monstruos y de los tesoros en el array que vamos a devolver
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                //TODO PARA CADA MONSTRUO 
                int num_monstruo = 0;
                ResetearDungeonArea();
                for (Celda monstruo : this.posicion_monstruos) {

                    if (((this.puertaEntrada.monstruo && i == 0)) && !this.dungeon[this.puertaEntrada.fila][this.puertaEntrada.columna].area_calculado) {

                        area = 0.0;

                        //TODO MODIFICAR EL NUMERADOR Y SUSTITUIRLO POR EL AREA
                        numerador = 1.0;
                        denominador = (f * c) - celdas_Paredes;

                        division = numerador / denominador;

                        //La celda de la puerta la establecemos como que hemos calculado su area
                        this.dungeon[this.puertaEntrada.fila][this.puertaEntrada.columna].area_calculado = true;

                        //se guarda el area calculado del monstruo
                        this.area_puerta_M[num_monstruo] = area;

                        //Se incrementa el monstruo a calcular
                        num_monstruo++;
                    } else {
                        ResetearDungeonCamino();

                        area_max = 0.0;
                        area = 0.0;
                        int distancia_max = -1;
                        this.distancia = -1;
                        llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, monstruo.fila, monstruo.columna);
                        distancia_max = this.distancia;

                        ArrayList<Celda> celdas_abiertas = new ArrayList<Celda>();
                        ArrayList<Celda> celdas_cerradas = new ArrayList<Celda>();

                        celdas_abiertas.add(this.puertaEntrada); //se anade el nodo inicial como celda visitada
                        Celda celda_A = new Celda();
                        //Mientras no se encuentre un monstruo u objeto se sigue buscando
                        while (celdas_abiertas.size() != 0) {

                            //guardo la primera posicion del array como la siguiente celda a la que voy a transitar
                            celda_A = celdaMenorF(celdas_abiertas);

                            // se anade la celda actual a la lista de celdas cerradas
                            celdas_cerradas.add(celda_A);
                            for (int celda = 0; celda < celdas_abiertas.size(); celda++) {
                                if (celdas_abiertas.get(celda).fila == celda_A.fila
                                        && celdas_abiertas.get(celda).columna == celda_A.columna) {
                                    celdas_abiertas.remove(celda);
                                }
                            }

                            // si la celda final es el destino paramos
                            if (celda_A.fila == monstruo.fila && celda_A.columna == monstruo.columna) {

                                break;
                            } else {
                                // obtenemos la lista de vecinos/adyacentes de la celda
                                // actual
                                ArrayList<Celda> vecinos = getlistaVecinos(celda_A);

                                // se recorren los vecinos de la celda actual
                                for (Celda vecino : vecinos) {

                                    // si la lista de celdas abiertas no contiene
                                    // al vecino ni la lista de cerradas
                                    int posicionAbiertas = -1;
                                    int posicionCerradas = -1;

                                    //Se recorre la lista de celdas abiertas para ver si existe la celda vecino
                                    for (int celda = 0; celda < celdas_abiertas.size(); celda++) {
                                        if (celdas_abiertas.get(celda).fila == vecino.fila
                                                && celdas_abiertas.get(celda).columna == vecino.columna) {
                                            posicionAbiertas = celda;
                                        }
                                    }

                                    //Se recorre la lista de celdas cerradas para ver si existe la celda vecino
                                    for (int celda = 0; celda < celdas_cerradas.size(); celda++) {
                                        if (celdas_cerradas.get(celda).fila == vecino.fila
                                                && celdas_cerradas.get(celda).columna == vecino.columna) {
                                            posicionCerradas = celda;
                                        }
                                    }

                                    //Si no esta la celda vecina entre las celdas abiertas y las cerradas, se anade a la lista de abiertas
                                    if (posicionAbiertas == -1 && posicionCerradas == -1) {

                                        //Se calcula la distancia entre la celda vecina y la celda de inicio
                                        llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, vecino.fila, vecino.columna);

                                        // Se anade a la lista de celdas abiertas la celda
                                        // vecina/adyacente
                                        //TODO IF de si el vecino respeta la distancia que queremos para añadirle a la lista de celdas abiertas
                                        if (this.distancia < distancia_max) {
                                            celdas_abiertas.add(vecino);
                                        }
                                    }
                                }
                            }
                        }

                        //area = celdas_recorridas.size();
                        for (Celda celda_segura : celdas_cerradas) {
                            int distancia_VP = -1;
                            this.distancia = -1;
                            llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, celda_segura.fila, celda_segura.columna);
                            distancia_VP = this.distancia;
                            if (distancia_VP < distancia_max) {
                                area = area + 1.0;
                            }
                        }

                        numerador = 1.0;
                        denominador = (this.f * this.c) - this.celdas_Paredes;

                        division = numerador / denominador;

                        //se guarda el area calculado del monstruo
                        this.area_puerta_M[num_monstruo] = area;

                        //Se incrementa el monstruo a calcular
                        num_monstruo++;
                    }

                }

            } //Si es la vuelta de los tesoros
            else if (i == 1) {
                //TODO PARA CADA TESORO 
                int num_tesoro = 0;
                ResetearDungeonArea();
                for (Celda tesoro : this.posicion_tesoros) {
                    if (((this.puertaEntrada.tesoro && i == 1)) && !this.dungeon[this.puertaEntrada.fila][this.puertaEntrada.columna].area_calculado) {

                        area = 0.0;

                        //TODO MODIFICAR EL NUMERADOR Y SUSTITUIRLO POR EL AREA
                        numerador = 1.0;
                        denominador = (f * c) - celdas_Paredes;

                        division = numerador / denominador;

                        //La celda de la puerta la establecemos como que hemos calculado su area
                        this.dungeon[this.puertaEntrada.fila][this.puertaEntrada.columna].area_calculado = true;

                        //se guarda el area calculado del monstruo
                        this.area_puerta_T[num_tesoro] = area;

                        //Se incrementa el tesoro a calcular
                        num_tesoro++;
                    } else {
                        ResetearDungeonCamino();

                        area_max = 0.0;
                        area = 0.0;
                        int distancia_max = -1;
                        this.distancia = -1;
                        llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, tesoro.fila, tesoro.columna);
                        distancia_max = this.distancia;

                        ArrayList<Celda> celdas_abiertas = new ArrayList<Celda>();
                        ArrayList<Celda> celdas_cerradas = new ArrayList<Celda>();

                        celdas_abiertas.add(this.puertaEntrada); //se anade el nodo inicial como celda visitada
                        Celda celda_A = new Celda();
                        //Mientras no se encuentre un monstruo u objeto se sigue buscando
                        while (celdas_abiertas.size() != 0) {

                            //guardo la primera posicion del array como la siguiente celda a la que voy a transitar
                            celda_A = celdaMenorF(celdas_abiertas);

                            // se anade la celda actual a la lista de celdas cerradas
                            celdas_cerradas.add(celda_A);
                            for (int celda = 0; celda < celdas_abiertas.size(); celda++) {
                                if (celdas_abiertas.get(celda).fila == celda_A.fila
                                        && celdas_abiertas.get(celda).columna == celda_A.columna) {
                                    celdas_abiertas.remove(celda);
                                }
                            }

                            // si la celda final es el destino paramos
                            if (celda_A.fila == tesoro.fila && celda_A.columna == tesoro.columna) {

                                break;
                            } else {
                                // obtenemos la lista de vecinos/adyacentes de la celda
                                // actual
                                ArrayList<Celda> vecinos = getlistaVecinos(celda_A);

                                // se recorren los vecinos de la celda actual
                                for (Celda vecino : vecinos) {

                                    // si la lista de celdas abiertas no contiene
                                    // al vecino ni la lista de cerradas
                                    int posicionAbiertas = -1;
                                    int posicionCerradas = -1;

                                    //Se recorre la lista de celdas abiertas para ver si existe la celda vecino
                                    for (int celda = 0; celda < celdas_abiertas.size(); celda++) {
                                        if (celdas_abiertas.get(celda).fila == vecino.fila
                                                && celdas_abiertas.get(celda).columna == vecino.columna) {
                                            posicionAbiertas = celda;
                                        }
                                    }

                                    //Se recorre la lista de celdas cerradas para ver si existe la celda vecino
                                    for (int celda = 0; celda < celdas_cerradas.size(); celda++) {
                                        if (celdas_cerradas.get(celda).fila == vecino.fila
                                                && celdas_cerradas.get(celda).columna == vecino.columna) {
                                            posicionCerradas = celda;
                                        }
                                    }

                                    //Si no esta la celda vecina entre las celdas abiertas y las cerradas, se anade a la lista de abiertas
                                    if (posicionAbiertas == -1 && posicionCerradas == -1) {

                                        //Se calcula la distancia entre la celda vecina y la celda de inicio
                                        llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, vecino.fila, vecino.columna);

                                        // Se anade a la lista de celdas abiertas la celda
                                        // vecina/adyacente
                                        //TODO IF de si el vecino respeta la distancia que queremos para añadirle a la lista de celdas abiertas
                                        if (this.distancia < distancia_max) {
                                            celdas_abiertas.add(vecino);
                                        }
                                    }
                                }
                            }
                        }

                        //area = celdas_recorridas.size();
                        for (Celda celda_segura : celdas_cerradas) {
                            int distancia_VP = -1;
                            this.distancia = -1;
                            llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, celda_segura.fila, celda_segura.columna);
                            distancia_VP = this.distancia;
                            if (distancia_VP < distancia_max) {
                                area = area + 1.0;
                            }

                        }

                        numerador = 1.0;
                        denominador = (this.f * this.c) - this.celdas_Paredes;

                        division = numerador / denominador;

                        //se guarda el area calculado del monstruo
                        this.area_puerta_T[num_tesoro] = area;

                        //Se incrementa el tesoro a calcular
                        num_tesoro++;
                    }
                }
            }
        }

        return fitness_puertas_total;
    }

    /**
     *
     * @param listaDesordenada
     * @return
     * @throws CloneNotSupportedException
     */
    public ArrayList<Celda> ordenarCeldas(ArrayList<Celda> listaDesordenada) throws CloneNotSupportedException {
        ArrayList<Celda> listaOrdenada = new ArrayList<Celda>();
        int distancia_PT = -1;
        int distancia_PT_anterior = -1;

        //se recorren los monstruos guardados
        for (int i = 0; i < listaDesordenada.size(); i++) {

            //si estamos en la primera vuelta, anadimos el recorrido a la 
            //lista de ordenados
            if (i == 0) {
                this.distancia = -1;

                if (this.puertaEntrada.fila == listaDesordenada.get(i).fila && this.puertaEntrada.columna == listaDesordenada.get(i).columna) {
                    llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, listaDesordenada.get(i).fila, listaDesordenada.get(i).columna);
                    distancia_PT = this.distancia;
                }

                llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, listaDesordenada.get(i).fila, listaDesordenada.get(i).columna);
                distancia_PT = this.distancia;

                if (this.distancia == -1) {
                    this.fitness = -100;
                }

                listaOrdenada.add((Celda) listaDesordenada.get(i).clone());
            } else {
                if (this.puertaEntrada.fila == listaDesordenada.get(i).fila && this.puertaEntrada.columna == listaDesordenada.get(i).columna) {
                    llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, listaDesordenada.get(i).fila, listaDesordenada.get(i).columna);
                    distancia_PT = this.distancia;
                }

                //se obtiene el recorrido
                this.distancia = -1;
                llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, listaDesordenada.get(i).fila, listaDesordenada.get(i).columna);

                if (this.distancia == -1) {
                    this.fitness = -100;
                }

                distancia_PT = this.distancia;

                //si se ha insertado algun elemento en los recorridos ordenados,
                //obtenemos la posicion donde hay que colocar al recorrido.
                if (listaOrdenada.size() >= 1) {
                    int pos = -1;
                    distancia_PT_anterior = -1;
                    //se recorre el array de los rec ordenados
                    for (int j = 0; j < listaOrdenada.size(); j++) {
                        //si el recorrido escogido es inferior al guardado entonces la posicion que le corresponde
                        //es donde se encuentra ese elemento
                        this.distancia = -1;
                        llegada_optima(this.puertaEntrada.fila, this.puertaEntrada.columna, listaOrdenada.get(j).fila, listaOrdenada.get(j).columna);
                        distancia_PT_anterior = this.distancia;

                        if (this.distancia == -1) {
                            this.fitness = -100;
                        }

                        //si la distancia es menor o igual 
                        if (distancia_PT <= distancia_PT_anterior && distancia_PT > -1 && distancia_PT_anterior > -1 && i > 0) {
                            pos = j;
                            j = listaOrdenada.size();
                        }
                    }

                    if (pos != -1) {
                        listaOrdenada.add(pos, (Celda) listaDesordenada.get(i).clone());
                    } else {
                        if (pos == -1 && (listaDesordenada.get(i).fila != this.puertaEntrada.fila || listaDesordenada.get(i).columna != this.puertaEntrada.columna)) {
                            listaOrdenada.add((Celda) listaDesordenada.get(i).clone());
                        } else if (pos == -1 && listaDesordenada.get(i).fila == this.puertaEntrada.fila && listaDesordenada.get(i).columna == this.puertaEntrada.columna) {
                            listaOrdenada.add(0, (Celda) listaDesordenada.get(i).clone());
                        }
                    }
                }
            }
        }

        return (ArrayList<Celda>) listaOrdenada.clone();
    }

    /**
     * Funcion que calcula el fitness del dungeon
     *
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    public void calcularfitness(int numero_puertas) throws CloneNotSupportedException {
        puertaEntradaEscogida = new String();
        //calculo el camino que hay entre la puerta 0 y el resto
        calcular_camino_PP(numero_puertas);

        //Calculo el fitness del dungeon siempre y cuando haya tesoros y monstruos 
        //en el mapa y haya camino entre la/s puerta/s y los objetos, sino tiene un fitness negativo
        if (numero_monstruos != 0 && numero_tesoros != 0 && this.dungeon_valido == true) {

            //los n primeros resultados son para el numero de mons y tes en el recorrido (uno para cada recorrido),
            //los dos siguientes son para el fitness del num de monstruos y de tesoros,
            //los restantes son para calcular el fitness de los monstruos, tesoros y area de seguridad de los tes.
            //el ultimo es para calcular el fitness de las celdas pared en el mapa
            int tamano_resultados = (int) (((numero_puertas - 1) * 2) + dificultad_nivel[2] + dificultad_nivel[3] + dificultad_nivel[3]) + 3;

            this.resultados = new double[tamano_resultados]; //variable que va a almacenar los resultados de las operaciones

            //Variable que va a guardar lo esperado de cara a luego poder ver bien los resultados
            this.distancias_esperadas = new double[tamano_resultados];

            //variable que va a almacenar los resultados a los que realmente se encuentran
            this.distancias_reales = new double[tamano_resultados];

            int posicion = 0; //variable para saber en la posicion del array que hay que poner las cosas

            //Se calcula el fitness del camino de la puerta de entrada con la puerta de salida mas cercana.
            if (numero_puertas > 1) {
                ArrayList<String[]> recorridoEscogido = null;
                recorridoEscogido = new ArrayList<String[]>();
                ArrayList<ArrayList<String[]>> recorridos_ordenados = null;
                recorridos_ordenados = new ArrayList<ArrayList<String[]>>(numero_puertas - 1);

                while (recorridos_ordenados.size() != this.recorridos.size()) {
                    //se recorren los recorridos guardados
                    for (int i = 0; i < this.recorridos.size(); i++) {
                        //si estamos en la primera vuelta, anadimos el recorrido a la 
                        //lista de ordenados
                        if (i == 0) {
                            recorridos_ordenados.add((ArrayList<String[]>) this.recorridos.get(i).clone());
                        } else {
                            //se obtiene el recorrido
                            recorridoEscogido = this.recorridos.get(i);

                            //si se ha insertado algun elemento en los recorridos ordenados,
                            //obtenemos la posicion donde hay que colocar al recorrido.
                            if (recorridos_ordenados.size() >= 1) {
                                int pos = -1;
                                //se recorre el array de los rec ordenados
                                for (int j = 0; j < recorridos_ordenados.size(); j++) {
                                    //si el recorrido escogido es inferior al guardado entonces la posicion que le corresponde
                                    //es donde se encuentra ese elemento
                                    if (recorridoEscogido.size() <= recorridos_ordenados.get(j).size()) {
                                        pos = j;
                                        j = recorridos_ordenados.size();
                                    }
                                }

                                if (pos != -1) {
                                    recorridos_ordenados.add(pos, (ArrayList<String[]>) this.recorridos.get(i).clone());
                                } else {
                                    recorridos_ordenados.add((ArrayList<String[]>) this.recorridos.get(i).clone());
                                }

                            }
                        }

                    }
                }

                //se ordenan los recorrido
                this.recorridos = (ArrayList<ArrayList<String[]>>) recorridos_ordenados.clone();

                double ponderacion_restante_r_m = ponderaciones_nivel[0] * 0.5;
                double ponderacion_restante_r_t = ponderaciones_nivel[1] * 0.5;
                double monstruos_recorrido_esperados = dificultad_nivel[0];
                double monstruos_sin_recolocar = dificultad_nivel[2];
                double tesoros_recorrido_esperados = dificultad_nivel[1];
                double tesoros_sin_recolocar = dificultad_nivel[3];

                int num_recorrido = 0;
                for (ArrayList<String[]> recorridoPP : this.recorridos) {

                    num_monstruos_recorrido = 0;
                    num_tesoros_recorrido = 0;
                    //Se recorre el recorrido para saber el numero de monstruos y tesoros q tiene en el camino.
                    for (int i = 0; i < recorridoPP.size(); i++) {
                        if (recorridoPP.get(i)[2].equals("M")) {
                            num_monstruos_recorrido++;
                        } else if (recorridoPP.get(i)[2].equals("T")) {
                            num_tesoros_recorrido++;
                        }
                    }

                    //si no hay recorrido lo penalizamos sino, ponemos el numero de monstruos y tesoros * ponderacion
                    if (recorridoPP.size() > 0) {
                        this.resultados[posicion] = (Math.abs(num_monstruos_recorrido - monstruos_recorrido_esperados) * ponderacion_restante_r_m);
                        this.distancias_esperadas[posicion] = monstruos_recorrido_esperados;
                        this.distancias_reales[posicion] = num_monstruos_recorrido;

                        posicion++;

                        this.resultados[posicion] = (Math.abs(num_tesoros_recorrido - tesoros_recorrido_esperados) * ponderacion_restante_r_t);
                        this.distancias_esperadas[posicion] = tesoros_recorrido_esperados;
                        this.distancias_reales[posicion] = num_tesoros_recorrido;
                    } else if (recorridoPP.isEmpty()) {
                        this.resultados[posicion] = ((f * c) * ponderacion_restante_r_m);
                        this.distancias_esperadas[posicion] = monstruos_recorrido_esperados;
                        this.distancias_reales[posicion] = num_monstruos_recorrido;

                        posicion++;

                        this.resultados[posicion] = ((f * c) * ponderacion_restante_r_m);
                        this.distancias_esperadas[posicion] = tesoros_recorrido_esperados;
                        this.distancias_reales[posicion] = num_tesoros_recorrido;
                    }

                    //TODO INCREMENTAR EL NUMERO DE MONSTRUOS Y TESOROS EN EL CAMINO SIGUIENTE
                    //se incrementa el numero de monstruos que deberia de haber en el camino siguiente
                    if ((num_recorrido < (this.recorridos.size() - 2)))//num_recorrido > 0 && (num_recorrido < (recorridos.size() - 2))) //si solo se incrementara a partir de la tercera puerta
                    {
                        monstruos_sin_recolocar = monstruos_sin_recolocar - monstruos_recorrido_esperados;
                        monstruos_recorrido_esperados = Math.round((monstruos_sin_recolocar) / (numero_puertas - num_recorrido - 1));

                        tesoros_sin_recolocar = tesoros_sin_recolocar - tesoros_recorrido_esperados;
                        tesoros_recorrido_esperados = Math.round((tesoros_sin_recolocar) / (numero_puertas - num_recorrido - 1));

                    } else if (num_recorrido < ((this.recorridos.size() - 1)))//nos encontramos en la penultima vuelta, por lo que se asignan el resto de monstruos
                    {
                        monstruos_recorrido_esperados = monstruos_sin_recolocar - monstruos_recorrido_esperados;
                        monstruos_sin_recolocar = monstruos_recorrido_esperados;

                        tesoros_recorrido_esperados = tesoros_sin_recolocar - tesoros_recorrido_esperados;
                        tesoros_sin_recolocar = tesoros_recorrido_esperados;
                    }

                    //se incrementa el numero de tesoros que deberia de haber en el camino siguiente
                    //tesoros_recorrido_esperados = Math.floor((numero_tesoros - tesoros_recorrido_esperados) / (numero_puertas - num_recorrido - 1));
                    //si no nos encontramos en el penultimo recorrido reducimos a la mitad la ponderacion que vamos a aplicar al siguiente recorrido
                    //e incrementamos el numero de monstruos y tesoros que deberian de estar en el camino siguiente
                    if (num_recorrido < (this.recorridos.size() - 2)) {
                        if (ponderacion_restante_r_m - (ponderacion_restante_r_m * 0.5) > 0) {
                            ponderacion_restante_r_m = ponderacion_restante_r_m - (ponderacion_restante_r_m * 0.5);
                        }

                        if (ponderacion_restante_r_t - (ponderacion_restante_r_t * 0.5) > 0) {
                            ponderacion_restante_r_t = ponderacion_restante_r_t - (ponderacion_restante_r_t * 0.5);
                        }

                    }
                    num_recorrido++;
                    posicion = posicion + 1;
                }
            } //si solo tenemos una puerta, entonces inicializamos la variable de de posicion
            else {
                posicion = posicion + 1;
            }

            //calculo el fitness de los tesoros
            calcular_fitness_tesoros();

            //calculo el fitness de las puertas
            calcular_fitness_puertas();

            if (this.fitness == -100) {
                return;
            }

            double porcent_mons = Math.abs((dificultad_nivel[2] * 50) / 100);
            double infer_mons = Math.abs(dificultad_nivel[2] - porcent_mons);
            double super_mons = Math.abs(dificultad_nivel[2] + porcent_mons);

            //Se comprueba si el numero de monstruos que hay en el mapa no esta por debajo ni por encima del 5% de lo esperado
//            if(this.posicion_monstruos.size() > dificultad_nivel[2]){
//                this.fitness = -100;
////                System.out.println("Mal monstruos");
//                return;
//            }
            this.resultados[posicion] = Math.abs(this.posicion_monstruos.size() - dificultad_nivel[2]) * ponderaciones_nivel[2] * dificultad_nivel[8];
            this.distancias_esperadas[posicion] = dificultad_nivel[2];
            this.distancias_reales[posicion] = this.posicion_monstruos.size();

            posicion = posicion + 1;

            double porcent_tes = Math.abs((dificultad_nivel[3] * 80) / 100);
            double infer_tes = Math.abs(dificultad_nivel[3] - porcent_tes);
            double super_tes = Math.abs(dificultad_nivel[3] + porcent_tes);

            //Se comprueba si el numero de tesoros que hay en el mapa no esta por debajo ni por encima del 5% de lo esperado
//            if((infer_tes > this.posicion_tesoros.size())){
//                this.fitness = -100;
//                System.out.println("Mal tesoros");
//                return;
//            }
//            if(this.posicion_tesoros.size() > dificultad_nivel[3]){
//                this.fitness = -100;
////                System.out.println("Mal tesoros");
//                return;
//            }
            this.resultados[posicion] = Math.abs(posicion_tesoros.size() - dificultad_nivel[3]) * ponderaciones_nivel[3] * dificultad_nivel[9];
            this.distancias_esperadas[posicion] = dificultad_nivel[3];
            this.distancias_reales[posicion] = this.posicion_tesoros.size();

//            //Seguridad montruos con la puerta de entrada ----------------------------------------------
//            //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
//            double ponderacion_restante_s_m = ponderaciones_nivel[4] * 0.5;
//            double distancia_esperada_s_m = dificultad_nivel[4];
//            for (int s_monstruos = 0; s_monstruos < dificultad_nivel[2]; s_monstruos++) {
//
//                posicion = posicion + 1;
//
//                //si la distancia esperada se pasa del numero de celdas libres igualamos la distancia a ese numero
//                if (distancia_esperada_s_m >= (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
//                    distancia_esperada_s_m = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
//                }
//
//                if (s_monstruos < this.posicion_monstruos.size()) {
//                    //si se encuentra en la misma celda que la puerta se le pone una penalizacion alta.
//                    if (area_puerta_M[s_monstruos] == 0.0 && distancia_esperada_s_m > 0.0) {
//                        this.fitness = -100;
//                        return;
////                        this.resultados[posicion] = ((f * c) * ponderacion_restante_s_m);
////                        this.distancias_esperadas[posicion] = this.posicion_monstruos.size();
////                        this.distancias_reales[posicion] = s_monstruos;
//                    } else {
////                        this.resultados[posicion] = (Math.abs(area_puerta_M[s_monstruos] - distancia_esperada_s_m) * ponderacion_restante_s_m); //se pone el 50 por ciento de la ponderacion
//                        if((Math.abs(area_puerta_M[s_monstruos] - distancia_esperada_s_m)) > (int)Math.round(((f * c) * 2)/100)){
//                            this.fitness = -100;
//                            return;
//                        }
//                        else{
//                            this.distancias_esperadas[posicion] = distancia_esperada_s_m;
//                            this.distancias_reales[posicion] = area_puerta_M[s_monstruos];
//                            this.resultados[posicion] = (Math.abs(area_puerta_M[s_monstruos] - distancia_esperada_s_m) * ponderacion_restante_s_m);
//                        }
//                        
//                    }
//
//                } else {
//                    this.resultados[posicion] = ((f * c) * ponderacion_restante_s_m);
//                    this.distancias_esperadas[posicion] = this.posicion_monstruos.size();
//                    this.distancias_reales[posicion] = -1;
//                    //resultados [posicion] = Math.abs(0 - distancia_esperada_s_m) * ponderacion_restante_s_m; //se pone el 50 por ciento de la ponderacion
//                }
//
//                //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
//                if (s_monstruos < (dificultad_nivel[2] - 2)) {
//                    ponderacion_restante_s_m = ponderacion_restante_s_m - (ponderacion_restante_s_m * 0.5);
//
//                }
//
//                //se incrementa la distancia a la que se espera el siguiente monstruo
//                if (Math.round(distancia_esperada_s_m + (0.5 * dificultad_nivel[4])) < (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
//                    distancia_esperada_s_m = Math.round(distancia_esperada_s_m + (0.5 * dificultad_nivel[4]));
//                } else {
//                    distancia_esperada_s_m = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
//                }
//
//            }
            if (this.fitness >= 0) {
                posicion = this.calcular_fitness_seguridad_P_mons(posicion);
            } else {
                this.fitness = -100;
                return;
            }

//            //Seguridad tesoros con la puerta de entrada ----------------------------------------------
//            //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
//            double ponderacion_restante_s_t = ponderaciones_nivel[5] * 0.5;
//            double distancia_esperada_s_t = dificultad_nivel[5];
//            for (int s_tesoros = 0; s_tesoros < dificultad_nivel[3]; s_tesoros++) {
//                posicion = posicion + 1;
//                
//                //si la distancia esperada se pasa del numero de celdas libres igualamos la distancia a ese numero			
//                if (distancia_esperada_s_t >= (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
//                    distancia_esperada_s_t = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
//                }
//
//                if (s_tesoros < posicion_tesoros.size()) {
//                    
//                    //si se encuentra en la misma celda que la puerta se le pone una penalizacion alta.
//                    if (area_puerta_T[s_tesoros] == 0 && distancia_esperada_s_t > 0) {
//                        this.resultados[posicion] = ((f * c) * ponderacion_restante_s_t);
//                        this.distancias_esperadas[posicion] = distancia_esperada_s_t;
//                        this.distancias_reales[posicion] = area_puerta_T[s_tesoros];
//                    } else {
//                        //solo se pondera si la distancia es inferior a la esperada
//                        this.resultados[posicion] = (Math.abs(area_puerta_T[s_tesoros] - distancia_esperada_s_t) * ponderacion_restante_s_t);
//                        this.distancias_esperadas[posicion] = distancia_esperada_s_t;
//                        this.distancias_reales[posicion] = area_puerta_T[s_tesoros];
//
//                    }
//
//                } else {
//
//                    //resultados [posicion] = Math.abs(0 - distancia_esperada_s_t) * ponderacion_restante_s_t;
//                    this.resultados[posicion] = ((f * c) * ponderacion_restante_s_t);
//                    this.distancias_esperadas[posicion] = posicion_tesoros.size();
//                    this.distancias_reales[posicion] = -1;
//                }
//
//                //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
//                if (s_tesoros < (dificultad_nivel[3] - 2)) {
//                    ponderacion_restante_s_t = ponderacion_restante_s_t - (ponderacion_restante_s_t * 0.5);
//                }
//
//                //se incrementa la distancia a la que se espera el siguiente tesoro
//                if (Math.round(distancia_esperada_s_t + (0.5 * dificultad_nivel[5])) < (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
//                    distancia_esperada_s_t = Math.round(distancia_esperada_s_t + (0.5 * dificultad_nivel[5]));
//                } else {
//                    distancia_esperada_s_t = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
//                }
//
//            }
            if (this.fitness >= 0) {
                posicion = this.calcular_fitness_seguridad_P_tes(posicion);
            } else {
                this.fitness = -100;
                return;
            }

//            //Seguridad tesoros con los monstruos ----------------------------------------------
//            //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
//            double ponderacion_restante_s_t_m = ponderaciones_nivel[6] * 0.5;
//            double distancia_esperada_s_t_m = dificultad_nivel[6];
//            for (int s_tes_mons = 0; s_tes_mons < dificultad_nivel[3]; s_tes_mons++) {
//                posicion = posicion + 1;
//                //si la ponderacion llega hasta 0 dejamos de decrementarla 
//                if (ponderacion_restante_s_t_m <= 0.0) {
//                    ponderacion_restante_s_t_m = 0.0;
//                }
//
//                //si la distancia esperada a la que se debe encontrar el monstruo es inferior o igual a 1 lo igualamos a 1 (distancia minima a la que puede estar un monstruo de un tesoro)
//                if (distancia_esperada_s_t_m <= 1) {
//                    distancia_esperada_s_t_m = 1;
//                }
//
//                if (s_tes_mons < posicion_tesoros.size()) {
//
//                    this.resultados[posicion] = (Math.abs(posicion_tesoros.get(s_tes_mons).distancia_seguridad_M - distancia_esperada_s_t_m) * ponderacion_restante_s_t_m);
//                    this.distancias_esperadas[posicion] = distancia_esperada_s_t_m;
//                    this.distancias_reales[posicion] = posicion_tesoros.get(s_tes_mons).distancia_seguridad_M;
//                } else {
//                    this.resultados[posicion] = ((f * c) * ponderacion_restante_s_t_m);
//                    this.distancias_esperadas[posicion] = posicion_tesoros.size();
//                    this.distancias_reales[posicion] = s_tes_mons;
//                }
//
//                //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
//                if (s_tes_mons < (dificultad_nivel[3] - 2)) {
//                    ponderacion_restante_s_t_m = ponderacion_restante_s_t_m - (ponderacion_restante_s_t_m * 0.5);
//
//                }
//
//                //se decrementa la distancia a la que se espera el monstruo del tesoro
//                if (Math.round(distancia_esperada_s_t_m - (0.25 * dificultad_nivel[6])) > 1) {
//                    distancia_esperada_s_t_m = Math.round(distancia_esperada_s_t_m - (0.25 * dificultad_nivel[6]));
//                } else {
//                    distancia_esperada_s_t_m = 1;
//                }
//
//            }
            if (this.fitness >= 0) {
                posicion = this.calcular_fitness_mons_tes(posicion);
            } else {
                this.fitness = -100;
                return;
            }

            posicion++;

            //se calcula el fitness del numero de celdas pared que hay en el mapa
            this.resultados[posicion] = Math.abs(celdas_Paredes - dificultad_nivel[7]) * ponderaciones_nivel[7];
            this.distancias_esperadas[posicion] = dificultad_nivel[7];
            this.distancias_reales[posicion] = celdas_Paredes;

            fitness = 0;
            //se suman todos los resultados para almacenar el fitness resultante
            for (int num_resultados = 0; num_resultados < resultados.length; num_resultados++) {
                fitness = fitness + this.resultados[num_resultados];
            }

        } else {
            this.dungeon_valido = false;

            fitness = -100;
        }

    }

    public int calcular_fitness_seguridad_P_mons(int posicion) {

        //Seguridad montruos con la puerta de entrada ----------------------------------------------
        //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
        double ponderacion_restante_s_m = ponderaciones_nivel[4] * 0.5;
        double distancia_esperada_s_m = dificultad_nivel[4];

        double celdas_vacias = (((this.f * this.c) - this.dificultad_nivel[7]) - this.dificultad_nivel[2] - dificultad_nivel[3]);

        for (int s_monstruos = 0; s_monstruos < dificultad_nivel[2]; s_monstruos++) {

            posicion = posicion + 1;

            //si la distancia esperada se pasa del numero de celdas libres igualamos la distancia a ese numero
            if (distancia_esperada_s_m >= (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
                distancia_esperada_s_m = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
            }

            if (s_monstruos < this.posicion_monstruos.size()) {

                //si el monstruo se encuentra mas cercano a la puerta de la 
                //distancia requerida se penaliza.
                if (this.area_puerta_M[s_monstruos] - distancia_esperada_s_m < 0) {
                    this.fitness = -100;
                    break;
                } else {
                    if ((this.area_puerta_M[s_monstruos] - distancia_esperada_s_m == 0)
                            || ((this.area_puerta_M[s_monstruos] - distancia_esperada_s_m) < Math.round((distancia_esperada_s_m * 30) / 100))) {
                        this.resultados[posicion] = 0.0;
                        this.distancias_esperadas[posicion] = distancia_esperada_s_m;
                        this.distancias_reales[posicion] = this.area_puerta_M[s_monstruos];
                    } else {
                        //solo se pondera si la distancia es inferior a la esperada
                        this.resultados[posicion] = (Math.abs(this.area_puerta_M[s_monstruos] - distancia_esperada_s_m) * ponderacion_restante_s_m);
                        this.distancias_esperadas[posicion] = distancia_esperada_s_m;
                        this.distancias_reales[posicion] = this.area_puerta_M[s_monstruos];
                    }
                }

            } else {
                this.resultados[posicion] = ((f * c) * ponderacion_restante_s_m);
                this.distancias_esperadas[posicion] = this.posicion_monstruos.size();
                this.distancias_reales[posicion] = -1;
            }

            //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
            if (s_monstruos < (dificultad_nivel[2] - 2)) {
                ponderacion_restante_s_m = ponderacion_restante_s_m - (ponderacion_restante_s_m * 0.5);

            }

            //se incrementa la distancia a la que se espera el siguiente monstruo
            if (Math.round(distancia_esperada_s_m + (0.5 * dificultad_nivel[4])) < (porcentaje - dificultad_nivel[2] - dificultad_nivel[3])) {
                distancia_esperada_s_m = Math.round(distancia_esperada_s_m + (0.5 * dificultad_nivel[4]));
            } else {
                distancia_esperada_s_m = (porcentaje - dificultad_nivel[2] - dificultad_nivel[3]);
            }

        }

        return posicion;
    }

    public int calcular_fitness_seguridad_P_tes(int posicion) {
        //Seguridad tesoros con la puerta de entrada ----------------------------------------------
        //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
        double ponderacion_restante_s_t = this.ponderaciones_nivel[5] * 0.5;
        double distancia_esperada_s_t = this.dificultad_nivel[5];
        for (int s_tesoros = 0; s_tesoros < (int) this.dificultad_nivel[3]; s_tesoros++) {
            posicion = posicion + 1;

            //si la distancia esperada se pasa del numero de celdas libres igualamos la distancia a ese numero			
            if (distancia_esperada_s_t >= (this.porcentaje - this.dificultad_nivel[2] - this.dificultad_nivel[3])) {
                distancia_esperada_s_t = (this.porcentaje - this.dificultad_nivel[2] - this.dificultad_nivel[3]);
            }

            if (s_tesoros < this.posicion_tesoros.size()) {

                //si el tesoro se encuentra mas cercano a la puerta de la 
                //distancia requerida se penaliza.
                if (this.area_puerta_T[s_tesoros] - distancia_esperada_s_t < 0) {
                    //solo se pondera si la distancia es inferior a la esperada
                    this.resultados[posicion] = (Math.abs(this.area_puerta_T[s_tesoros] - distancia_esperada_s_t) * ponderacion_restante_s_t);
                    this.distancias_esperadas[posicion] = distancia_esperada_s_t;
                    this.distancias_reales[posicion] = this.area_puerta_T[s_tesoros];
                } else {
                    this.resultados[posicion] = 0.0;
                    this.distancias_esperadas[posicion] = distancia_esperada_s_t;
                    this.distancias_reales[posicion] = this.area_puerta_T[s_tesoros];
                }

                //si se encuentra en la misma celda que la puerta se le pone una penalizacion alta.
//                    if (this.area_puerta_T[s_tesoros] == 0 && distancia_esperada_s_t > 0) {
//                        this.resultados[posicion] = ((this.f * this.c) * ponderacion_restante_s_t);
//                        this.distancias_esperadas[posicion] = distancia_esperada_s_t;
//                        this.distancias_reales[posicion] = this.area_puerta_T[s_tesoros];
//                    } else {
//                        //solo se pondera si la distancia es inferior a la esperada
//                        this.resultados[posicion] = (Math.abs(this.area_puerta_T[s_tesoros] - distancia_esperada_s_t) * ponderacion_restante_s_t);
//                        this.distancias_esperadas[posicion] = distancia_esperada_s_t;
//                        this.distancias_reales[posicion] = this.area_puerta_T[s_tesoros];
//                    }
            } else {

                //resultados [posicion] = Math.abs(0 - distancia_esperada_s_t) * ponderacion_restante_s_t;
                this.resultados[posicion] = ((this.f * this.c) * ponderacion_restante_s_t);
                this.distancias_esperadas[posicion] = this.posicion_tesoros.size();
                this.distancias_reales[posicion] = -1;
            }

            //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
            if (s_tesoros < (this.dificultad_nivel[3] - 2)) {
                ponderacion_restante_s_t = ponderacion_restante_s_t - (ponderacion_restante_s_t * 0.5);
            }

            //se incrementa la distancia a la que se espera el siguiente tesoro
            if (Math.round(distancia_esperada_s_t + (0.5 * this.dificultad_nivel[5])) < (this.porcentaje - this.dificultad_nivel[2] - this.dificultad_nivel[3])) {
                distancia_esperada_s_t = Math.round(distancia_esperada_s_t + (0.5 * this.dificultad_nivel[5]));
            } else {
                distancia_esperada_s_t = (this.porcentaje - this.dificultad_nivel[2] - this.dificultad_nivel[3]);
            }

        }

        return posicion;
    }

    public int calcular_fitness_mons_tes(int posicion) {
        //Seguridad tesoros con los monstruos ----------------------------------------------
        //inicializamos la ponderacion restante al 50% de la ponderacion total para todos los monstruos
        double ponderacion_restante_s_t_m = ponderaciones_nivel[6] * 0.5;
        double distancia_esperada_s_t_m = dificultad_nivel[6];
        for (int s_tes_mons = 0; s_tes_mons < (int) dificultad_nivel[3]; s_tes_mons++) {
            posicion = posicion + 1;
            //si la ponderacion llega hasta 0 dejamos de decrementarla 
            if (ponderacion_restante_s_t_m <= 0.0) {
                ponderacion_restante_s_t_m = 0.0;
            }

            //si la distancia esperada a la que se debe encontrar el monstruo es inferior o igual a 1 lo igualamos a 1 (distancia minima a la que puede estar un monstruo de un tesoro)
            if (distancia_esperada_s_t_m <= 1) {
                distancia_esperada_s_t_m = 1;
            }

            if (s_tes_mons < posicion_tesoros.size()) {

                this.resultados[posicion] = (Math.abs(posicion_tesoros.get(s_tes_mons).distancia_seguridad_M - distancia_esperada_s_t_m) * ponderacion_restante_s_t_m);
                this.distancias_esperadas[posicion] = distancia_esperada_s_t_m;
                this.distancias_reales[posicion] = posicion_tesoros.get(s_tes_mons).distancia_seguridad_M;
            } else {
                this.resultados[posicion] = ((f * c) * ponderacion_restante_s_t_m);
                this.distancias_esperadas[posicion] = posicion_tesoros.size();
                this.distancias_reales[posicion] = s_tes_mons;
            }

            //si nos encontramos en la penultima iteracion del bucle no modificamos la ponderacion (los dos ultimos elementos tienen la misma ponderacion), sino la decrementamos un 50 %
            if (s_tes_mons < (dificultad_nivel[3] - 2)) {
                ponderacion_restante_s_t_m = ponderacion_restante_s_t_m - (ponderacion_restante_s_t_m * 0.5);

            }

            //se decrementa la distancia a la que se espera el monstruo del tesoro
            if (Math.round(distancia_esperada_s_t_m - (0.25 * dificultad_nivel[6])) > 1) {
                distancia_esperada_s_t_m = Math.round(distancia_esperada_s_t_m - (0.25 * dificultad_nivel[6]));
            } else {
                distancia_esperada_s_t_m = 1;
            }

        }
        return posicion;
    }

    /**
     * Funcion que devuelve una lista de los vecinos transitables de la celda en
     * la que nos encontramos
     *
     * @param celda_actual celda donde nos encontramos
     * @return lista vecinos transitables de la celda actual
     */
    public ArrayList<Celda> getlistaVecinos(Celda celda_actual) {
        //Nos creamos un arraylist de celda para guardar los vecinos de la celda que recibimos 
        ArrayList<Celda> vecinos_celda_actual = new ArrayList<Celda>();

        // Para cada vecino de la celda actual
        for (Celda tipo_celda : celda_actual.Vecinos)//recorrer la lista de paredes de la celda actual
        {
            // nos creamos un array unidimensional de integers que va a guardar las coordenadas de la siguiente celda a la que vamos a transitar
            int[] siguientePosicion = new int[2];
            siguientePosicion[0] = tipo_celda.fila;
            siguientePosicion[1] = tipo_celda.columna;
            // Anadimos a la lista de vecinos posibles a los que podemos transitar pas‡ndole las coordenadas que hemos recibido anteriormentes
            vecinos_celda_actual.add(this.dungeon[siguientePosicion[0]][siguientePosicion[1]]);
        }

        return vecinos_celda_actual; // devolvemos el vecino al que podriamos transitar 
    }

    /**
     * Funcion que va recorriendo el camino que hemos hecho desde la meta hasta
     * la celda inicial
     *
     * @param posicion_meta posicion final
     */
    public void RecorrerCamino(Celda posicion_meta, Celda posicion_inicio) {

        Boolean caminoPP = false;

        if (posicion_meta.puerta) {
            caminoPP = true;
            this.recorrido = null;
            this.recorrido = new ArrayList<String[]>();
        }

        // Creamos una celda y guardamos la posicion de meta en ella ya que empezamos por el final
        Celda celda_camino = posicion_meta;

        //si la celda siguiente es el destino, la distancia es de uno, sino continuamos
        if (celda_camino.fila == posicion_meta.Posicion_precursor[0] && celda_camino.columna == posicion_meta.Posicion_precursor[1]) {
            distancia = 1;
        } else {
            distancia = 0;

            //mientras la celda camino no sea la celda precursora a la celda destino, continuamos
            while (celda_camino.Posicion_precursor[0] != -1 && celda_camino.Posicion_precursor[1] != -1) {
                //Nos creamos una celda que la igualamos a la celda precursora de la celda camino
                Celda celda_precursora = this.dungeon[celda_camino.Posicion_precursor[0]][celda_camino.Posicion_precursor[1]];

                //si estamos calculando el camino entre una puerta y otra entonces guardamos la info de las celdas del recorrido
                if (caminoPP) {
                    if (this.dungeon[celda_camino.fila][celda_camino.columna].monstruo) {
                        String[] position = new String[3];

                        position[0] = Integer.toString(celda_camino.fila);
                        position[1] = Integer.toString(celda_camino.columna);
                        position[2] = "M";
                        this.recorrido.add(position);
                    } else if (this.dungeon[celda_camino.fila][celda_camino.columna].tesoro) {
                        String[] position = new String[3];
                        position[0] = Integer.toString(celda_camino.fila);
                        position[1] = Integer.toString(celda_camino.columna);
                        position[2] = "T";
                        this.recorrido.add(position);
                    } //si no hay ni un monstruo ni un tesoro lo anadimos como celda free
                    else if (!this.dungeon[celda_camino.fila][celda_camino.columna].tesoro && !this.dungeon[celda_camino.fila][celda_camino.columna].monstruo) {
                        String[] position = new String[3];
                        position[0] = Integer.toString(celda_camino.fila);
                        position[1] = Integer.toString(celda_camino.columna);
                        position[2] = " ";
                        this.recorrido.add(position);
                    }

                }

                celda_camino = celda_precursora; // continuamos con el recorrrido

                distancia++; //anado un movimiento a la distancia

            }
        }

    }

    /**
     * Funcion que recorre el dungeon y va reseteando el camino que hayas hecho
     * anteriormente para volver a calcular un camino PT
     */
    public void ResetearDungeonCamino() {
        for (int i = 0; i < this.dungeon.length; i++) {
            for (int j = 0; j < this.dungeon[i].length; j++) {
                //resetamos los booleanos y los ponemos a false de nuevo por si estamos volviendo de nuevo a pasar por este dungeon
                this.dungeon[i][j].inicio = false;
                this.dungeon[i][j].camino = false;
                this.dungeon[i][j].destino = false;
                this.dungeon[i][j].Posicion_precursor = new int[2];
                this.dungeon[i][j].Posicion_precursor[0] = -1;
                this.dungeon[i][j].Posicion_precursor[1] = -1;
                this.dungeon[i][j].setF(0);
                this.dungeon[i][j].setH(0);
                this.dungeon[i][j].setG(0);
            }
        }
    }

    /**
     * Funcion que recorre el dungeon y va reseteando el camino que hayas hecho
     * anteriormente para volver a calcular un camino PT
     */
    public void ResetearDungeon() {
        for (int i = 0; i < this.dungeon.length; i++) {
            for (int j = 0; j < this.dungeon[i].length; j++) {
                //resetamos los booleanos y los ponemos a false de nuevo por si estamos volviendo de nuevo a pasar por este dungeon
                this.dungeon[i][j].inicio = false;
                this.dungeon[i][j].camino = false;
                this.dungeon[i][j].destino = false;

                this.dungeon[i][j].tesoro = false;
                this.dungeon[i][j].monstruo = false;
//				this.dungeon[i][j].pared = false;

                // decimos que la celda ha sido visitada
                this.dungeon[i][j].visitada = false;

                this.dungeon[i][j].distancia_seguridad_M = -1;

                this.dungeon[i][j].Vecinos = new ArrayList<Celda>(4);

                this.dungeon[i][j].tam_dist_T = new ArrayList<Integer>();

                this.dungeon[i][j].tam_dist_M = new ArrayList<Integer>();

                // Inicializamos el array de int que luego va a guardar las coordenadas de su precursora
                this.dungeon[i][j].Posicion_precursor = new int[2];

                // Inicializamos el array de int que luego va a guardar las coordenadas del tesoro mas cercano
                this.dungeon[i][j].Posicion_T_cercano = new int[2];

                // Inicializamos el array de int que luego va a guardar las coordenadas del monstruo mas cercano
                this.dungeon[i][j].Posicion_M_cercano = new int[2];

                this.dungeon[i][j].distancia_P_cercana = -1;

                for (int k = 0; k < this.dungeon[i][j].genotipo_celda.length; k++) {
                    this.dungeon[i][j].genotipo_celda[k] = -1;
                }

            }
        }

    }

    public void ResetearCelda(Celda celda) {
        //resetamos los booleanos y los ponemos a false de nuevo por si estamos volviendo de nuevo a pasar por este dungeon
        this.dungeon[celda.fila][celda.columna].destino = false;
        this.dungeon[celda.fila][celda.columna].tesoro = false;
        this.dungeon[celda.fila][celda.columna].monstruo = false;

        // decimos que la celda ha sido visitada
        this.dungeon[celda.fila][celda.columna].visitada = false;

        this.dungeon[celda.fila][celda.columna].distancia_seguridad_M = -1;

        this.dungeon[celda.fila][celda.columna].Vecinos = new ArrayList<Celda>(4);

        this.dungeon[celda.fila][celda.columna].tam_dist_T = new ArrayList<Integer>();

        this.dungeon[celda.fila][celda.columna].tam_dist_M = new ArrayList<Integer>();

        // Inicializamos el array de int que luego va a guardar las coordenadas de su precursora
        this.dungeon[celda.fila][celda.columna].Posicion_precursor = new int[2];

        // Inicializamos el array de int que luego va a guardar las coordenadas del tesoro mas cercano
        this.dungeon[celda.fila][celda.columna].Posicion_T_cercano = new int[2];

        // Inicializamos el array de int que luego va a guardar las coordenadas del monstruo mas cercano
        this.dungeon[celda.fila][celda.columna].Posicion_M_cercano = new int[2];

        this.dungeon[celda.fila][celda.columna].distancia_P_cercana = -1;

    }

    /**
     * Funcion que recorre el dungeon y va reseteando las celdas que han sido
     * marcadas para continuar calculando el area
     */
    public void ResetearDungeonArea() {
        for (int i = 0; i <= this.dungeon.length - 1; i++) {
            for (int j = 0; j <= this.dungeon[i].length - 1; j++) {
                //resetamos los booleanos y los ponemos a false de nuevo para poder volver a poner areas en este mapa
                this.dungeon[i][j].area_calculado = false;

            }
        }

    }

    public void set_fitness(int fitness_modificado) {
        fitness = fitness_modificado;
    }

    /**
     * Funcion que pinta el dungeon en ASCI
     */
    public void pintar() {

        for (int i = 0; i <= this.dungeon.length - 1; i++) {
            //Pintamos los techos con este for
            for (int j = 0; j <= this.dungeon[i].length; j++) {
                // si nos encontramos en la fila inicial, todas tienen techos cerrados, pues no se puede abrir esas paredes
                if (j < this.dungeon[i].length && i == 0) {
                    System.out.print("+---");
                }

                // si estamos en otra fila que no sea la primera entramos en este if
                if (j < this.dungeon[i].length && i != 0) {
                    //pintamos un mas cada vez que entramos para separar las celdas y poner las esquinas de cada una con un mas
                    System.out.print("+");

                    // si la pared norte de la celda en la que estamos est‡ abierta entonces pintamos espacios, sino pintamos lineas para mostrar que est‡ cerrada
                    System.out.print("---");

                }

                // si hemos llegado al final de la fila pintamos el mas final
                if (j == this.dungeon[i].length) {
                    System.out.println("+");
                }
            }

            //Pintamos las paredes con este for
            for (int j = 0; j <= this.dungeon[i].length; j++) {

                // si nos encontramos en el lado de la izquierda pintamos pared y espacios o, dependiendo de si es la celda salida, meta o camino pintamos una letra representativa en vez de un espacio en esa celda
                if (j < this.dungeon[i].length) {
                    if (this.dungeon[i][j].genotipo_celda[0] == 0 && this.dungeon[i][j].genotipo_celda[1] == 0 && this.dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda vacia no pintamos nada
                    {
                        System.out.print("|   ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 0 && this.dungeon[i][j].genotipo_celda[1] == 0 && this.dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene un gigante se pinta una G
                    {
                        System.out.print("| G ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 0 && this.dungeon[i][j].genotipo_celda[1] == 1 && this.dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene un murcielago se pinta una M
                    {
                        System.out.print("| M ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 0 && this.dungeon[i][j].genotipo_celda[1] == 1 && this.dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una moneda se pinta una C
                    {
                        System.out.print("| C ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 1 && this.dungeon[i][j].genotipo_celda[1] == 0 && this.dungeon[i][j].genotipo_celda[2] == 0) //Si es una celda que tiene un soldado se pinta una S
                    {
                        System.out.print("| S ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 1 && this.dungeon[i][j].genotipo_celda[1] == 0 && this.dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una llave se pinta una L
                    {
                        System.out.print("| L ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 1 && this.dungeon[i][j].genotipo_celda[1] == 1 && this.dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene una piedra preciosa se pinta una P
                    {
                        System.out.print("| P ");
                    } else if (this.dungeon[i][j].genotipo_celda[0] == 1 && this.dungeon[i][j].genotipo_celda[1] == 1 && this.dungeon[i][j].genotipo_celda[2] == 1) //Si es una celda que tiene muro se pinta una O
                    {
                        System.out.print("| * ");
                    }
                }

                // si hemos llegado al final de la fila pintamos la barra final y un salto de linea para seguir pintando en la siguiente linea
                if (j == this.dungeon[i].length) {
                    System.out.println("|");
                }

            }
            // si las x son igual a la dimension del dungeon -1, es decir, que hemos llegado a la ultima fila del dungeon, pintamos el suelo del dungeon que va a estar cerrado 
            if (i == this.dungeon.length - 1) {
                for (int h = 0; h < this.dungeon[i].length; h++) {
                    System.out.print("+---");

                    if (h == this.dungeon[i].length - 1) {
                        System.out.println("+");
                    }
                }
            }

        }

        //SE PINTAN LAS CARACTERISTICAS DE LA HABITACION
        System.out.println(" ");
        System.out.println("-------------");
        System.out.println("Genotipo del individuo: ");
        int posicion = 0;
        for (int celda = 0; celda < (genotipo.length / tipo_celdas); celda++) {
            for (int cont = 0; cont < tipo_celdas; cont++) {
                System.out.print(genotipo[posicion]);
                posicion++;
            }
            System.out.print(" ");

        }
        System.out.println(" ");
        System.out.println("-------------");

        System.out.println(" ");
        System.out.println("-------------");
        System.out.println("Numero de monstruos     : " + this.numero_monstruos);

        for (int i = 0; i < this.posicion_monstruos.size(); i++) {
            System.out.print("[" + this.posicion_monstruos.get(i).fila + " " + this.posicion_monstruos.get(i).columna + "]");
        }
        System.out.println("");
        System.out.println("");

        System.out.println("Numero de tesoros       : " + this.numero_tesoros);

        for (int i = 0; i < this.posicion_tesoros.size(); i++) {
            System.out.print("[" + this.posicion_tesoros.get(i).fila + " " + this.posicion_tesoros.get(i).columna + "]");
        }
        System.out.println("");
        System.out.println("");

        System.out.println("Numero de celdas Pared  : " + this.celdas_Paredes);
        System.out.println("Numero de celdas Libres : " + ((this.f * this.c) - this.celdas_Paredes - this.numero_monstruos - this.numero_tesoros));
        System.out.println("-------------");

        System.out.println(" ");
        System.out.println("-------------");
        System.out.println("Tipo puertas: ");

        for (int fila = 0; fila < f; fila++) {
            for (int columna = 0; columna < c; columna++) {
                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_N) //Si es puerta norte
                {
                    System.out.println("Puerta Norte " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_N + " ");
                    System.out.println(" ");
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_S)//Si es puerta sur
                {
                    System.out.println("Puerta Sur   " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_S + " ");
                    System.out.println(" ");
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_E)//Si es puerta este
                {
                    System.out.println("Puerta Este  " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_E + " ");
                    System.out.println(" ");
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_O)//Si es puerta oeste
                {
                    System.out.println("Puerta Oeste " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_O + " ");
                    System.out.println(" ");
                }
            }
        }

        System.out.println("Puerta Entrada Escogida: " + puertaEntradaEscogida);

        System.out.println("-------------");

        /*
            System.out.println(" ");
            System.out.println("Status individuo: "+ dungeon_valido);
         */
        if (this.resultados != null) {

            for (int i = 0; i < this.recorridos.size(); i++) {
                System.out.println("Recorrido " + i + ", tamano = " + this.recorridos.get(i).size() + " :");
                for (int j = 0; j < this.recorridos.get(i).size(); j++) {
                    System.out.print("[" + this.recorridos.get(i).get(j)[0] + " " + this.recorridos.get(i).get(j)[1] + "]");
                }
                System.out.println("");
                for (int j = 0; j < this.recorridos.get(i).size(); j++) {
                    System.out.print("[ " + this.recorridos.get(i).get(j)[2] + " ]");
                }
                System.out.println("");
                System.out.println("");

            }

            System.out.println("");

            System.out.println("Desglose fitness: ");

            for (int i = 0; i < this.resultados.length; i++) {
                System.out.println("Resultado " + i + ":" + "real (" + this.distancias_reales[i] + ") y esperado (" + this.distancias_esperadas[i] + ")" + this.resultados[i]);
            }
        }

        System.out.println(" ");
        System.out.println("Fitness: " + this.fitness);
        System.out.println("-----------------------");

    } // Cierra la funcion pintar

    public String getDungeonInfo() {

        String gen_txt;
        gen_txt = "------------- \n"
                + "Genotipo del individuo: \n";

        int posicion = 0;
        for (int celda = 0; celda < (genotipo.length / tipo_celdas); celda++) {
            for (int cont = 0; cont < tipo_celdas; cont++) {
                gen_txt = gen_txt + genotipo[posicion];
                posicion++;
            }
            gen_txt = gen_txt + " ";

        }
        gen_txt = gen_txt + " \n";
        gen_txt = gen_txt + "------------- \n";

        String n_mons_txt;

        n_mons_txt = " \n"
                + "------------- \n"
                + "Numero de monstruos     : " + this.numero_monstruos + "\n";

        for (int i = 0; i < this.posicion_monstruos.size(); i++) {
            n_mons_txt = n_mons_txt
                    + "[" + this.posicion_monstruos.get(i).fila
                    + " " + this.posicion_monstruos.get(i).columna + "]";
        }
        n_mons_txt = n_mons_txt + " \n";
        n_mons_txt = n_mons_txt + " \n";

        //*****************************************************************************
        //*****************************************************************************
        //*****************************************************************************
        //*****************************************************************************
        String n_tes_txt;
        n_tes_txt = "Numero de tesoros       : " + this.numero_tesoros + "\n";

        for (int i = 0; i < this.posicion_tesoros.size(); i++) {
            n_tes_txt = n_tes_txt
                    + "[" + this.posicion_tesoros.get(i).fila
                    + " " + this.posicion_tesoros.get(i).columna + "]";
        }
        n_tes_txt = n_tes_txt + " \n";
        n_tes_txt = n_tes_txt + " \n";

        String n_cel_pared_txt = "Numero de celdas Pared  : " + this.celdas_Paredes + " \n";
        String n_cel_libres_txt = "Numero de celdas Libres : " + ((this.f * this.c) - this.celdas_Paredes - this.numero_monstruos - this.numero_tesoros) + " \n";
        n_cel_libres_txt = n_cel_libres_txt + "-------------" + " \n";

        String tip_puertas_txt = " \n";
        tip_puertas_txt = tip_puertas_txt + "------------- \n";
        tip_puertas_txt = tip_puertas_txt + "Tipo puertas: \n";

        for (int fila = 0; fila < f; fila++) {
            for (int columna = 0; columna < c; columna++) {
                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_N) //Si es puerta norte
                {
                    tip_puertas_txt += "Puerta Norte " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_N + " \n";
                    tip_puertas_txt += " \n";
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_S)//Si es puerta sur
                {
                    tip_puertas_txt += "Puerta Sur   " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_S + " \n";
                    tip_puertas_txt += " \n";
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_E)//Si es puerta este
                {
                    tip_puertas_txt += "Puerta Este  " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_E + " \n";
                    tip_puertas_txt += " \n";
                }

                if (this.dungeon[fila][columna].puerta && this.dungeon[fila][columna].puerta_O)//Si es puerta oeste
                {
                    tip_puertas_txt += "Puerta Oeste " + fila + " " + columna + ": " + this.dungeon[fila][columna].tipo_puerta_O + " \n";
                    tip_puertas_txt += " \n";
                }
            }
        }

        String entrada_escog_txt = "Puerta Entrada Escogida: " + puertaEntradaEscogida + " \n";
        entrada_escog_txt += "------------- \n";

        String desglose_txt = " \n";
        if (this.resultados != null) {

            desglose_txt += "Desglose fitness: " + " \n";

            for (int i = 0; i < this.resultados.length; i++) {
                desglose_txt += "Resultado " + i + ":" + "real (" + this.distancias_reales[i] + ") y esperado (" + this.distancias_esperadas[i] + ")" + this.resultados[i] + " \n";
            }
        }

        //*******************************************************************************************
        //*******************************************************************************************
        //*******************************************************************************************
        //*******************************************************************************************
        String fitness_txt = " \n";
        fitness_txt += "Fitness: " + this.fitness + " \n";
        fitness_txt += "-----------------------" + " \n";

        String info = java.text.MessageFormat.format("{0}" + "{1}" + "{2}"
                + "{3}" + "{4}" + "{5}" + "{6}" + "{7}" + "{8}",
                gen_txt, n_mons_txt, n_tes_txt, n_cel_pared_txt,
                n_cel_libres_txt, tip_puertas_txt, entrada_escog_txt,
                desglose_txt, fitness_txt);

        return info;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}//Cierra la clase
