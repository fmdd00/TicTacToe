package pe.edu.ulima.pm.tictactoe

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Declaramos las variables que hacen referencia a los botones, para que estos sean accesibles en
    //en el programa
    lateinit var a1 : Button
    lateinit var b1 : Button
    lateinit var c1 : Button
    lateinit var a2 : Button
    lateinit var b2 : Button
    lateinit var c2 : Button
    lateinit var a3 : Button
    lateinit var b3 : Button
    lateinit var c3 : Button

    //Lo siguiente es para poder intercalar entre cada simbolo cuando se presione el boton
    //Con la variable btnPres, definida más abajo, se busca que cuando el botón sea presionado, se
    //muestre el simbolo correspondiente con el jugador activo, por lo cual definimos 3 variables:
    //jugador1, jugador y jugadorAct, que iniciara siendo el jugador 1

    //Declaramos tambien la variable del textView que indica el turno para que se cambie el texto
    //que se muestra en pantalla con el turno que le pertenece al jugador siguiente

    lateinit var turno : TextView
    var jugador1 = 0
    var jugador2 = 1
    var jugadorAct = jugador1



    //Para evitar que se pueda presionar un boton mas de una vez, causando que este cambie de valor,
    //crearemos la opcion de bloquear el boton ya presionado con un array bloqBtn
    lateinit var bloqBtn : IntArray

    //Definimos la variable juegoActivo como true para indicar que el juego puede ser jugado actualmente.
    //El valor de esta variable pasara a ser false cuando se hayan acabado los turnos o cuando ya se tenga
    //un ganador
    var juegoActivo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ya que se le asigno un tag que va del 0 al 8 a cada boton, crearemos un array llamado
        //bloqBtn en donde se almacena el estado de -1, así que cuando se presione
        bloqBtn = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1)

        //Almacenamos los id de los componentes en sus respectivas variables, para poder acceder a
        //ellos cuando sea necesario
        turno = findViewById(R.id.turno)
        a1 = findViewById(R.id.a1)
        b1 = findViewById(R.id.b1)
        c1 = findViewById(R.id.c1)
        a2 = findViewById(R.id.a2)
        b2 = findViewById(R.id.b2)
        c2 = findViewById(R.id.c2)
        a3 = findViewById(R.id.a3)
        b3 = findViewById(R.id.b3)
        c3 = findViewById(R.id.c3)

        //Preparamos cada boton para que sea utilizable por la funcion onClick() que actuará cuando
        //un boton sea presionado
        a1.setOnClickListener(this)
        a2.setOnClickListener(this)
        a3.setOnClickListener(this)
        b1.setOnClickListener(this)
        b2.setOnClickListener(this)
        b3.setOnClickListener(this)
        c1.setOnClickListener(this)
        c2.setOnClickListener(this)
        c3.setOnClickListener(this)

    }

    //La siguiente funcion permite realizar una accion cuando un boton es presionado. Para saber el
    //boton presionado, usamos findViewById<Button> y lo almacenamos en una variable btnPres
    //Luego, como definimos que el juego comienza con el turno del jugador1, se le asigna el simbolo
    //correspondiente al boton presionado por el jugador, cambiando el texto del boton con el id conseguido
    //anteriormente en la variable btnPres

    //Creamos la funcion onClick()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {

        //Primero comprobamos que el juego siga activo, por lo cual nos aseguramos que el valor de
        //juegoActivo sea true, si es false, entonces terminara el programa con el return
        if(!juegoActivo) {
            return
        }

        //Esta variable almacenará el id del boton que ha sido presionado
        var btnPres = findViewById<Button>(v!!.id)

        //Cuando se presione un boton, se almacenara la etiqueta que le pertenece en la variable tagPres
        //Como se quiere conseguir el numero de la etiqueta/tag, entonces empleamnos Integer.parseInt
        var tagPres = Integer.parseInt(btnPres.tag.toString())

        //Si en el array se tiene que en la posicion actual no existe el valor de -1, entonces
        //queremos que no retorne nada. En otras palabras, si el valor es -1, eso indica que el
        //boton no ha sido presionado. Cuando se presione el boton, se cambiará su posicion en el
        //array bloqBtn a 0 o 1 dependiendo del jugador, y tras esto, el boton queda bloqueado y ya
        //no se podrá cambiar el valor
        if(bloqBtn[tagPres] != -1) {
            return
        }

        //Se asigna el valor de jugadorAct, que puede ser 0 o 1, al array bloqBtn en la posicion conseguida
        //con la variable tagPres
        bloqBtn[tagPres] = jugadorAct

        //Si el jugador es el 1, entonces se asigna la O, y si es el 2, la X
        //Tras cada turno, se cambia el estado de jugador activo al siguiente jugador
        if(jugadorAct == jugador1) {
            btnPres.setText("O")
            jugadorAct = jugador2
            //Cambiamos el texto del textview llamado turno para indicar el turno del sgte jugador
            turno.setText("Turno del jugador X")
            //Cambia el color del texto a negro
            btnPres.setTextColor(Color.BLACK)
            //Cambia el color del boton a amarillo
            btnPres.backgroundTintList = getColorStateList(R.color.yellow)
        }else{
            btnPres.setText("X")
            jugadorAct = jugador1
            //Cambiamos el texto del textview llamado turno para indicar el turno del sgte jugador
            turno.setText("Turno del jugador O")
            //Cambia el color del texto a negro
            btnPres.setTextColor(Color.BLACK)
            //Cambia el color del boton a verde
            btnPres.backgroundTintList = getColorStateList(R.color.green)
        }

        //Se llama a la funcion definirGanador() que eavaluara las posiciones de posible victoria y
        //las comparara con el resultado de la partida
        definirGanador()
    }

    //Creamos la funcion definirGanador()
    @RequiresApi(Build.VERSION_CODES.M)
    private fun definirGanador() {
        //Se define al ganador cuando el jugador haya puesto su simbolo en las posiciones:
        //0 1 2
        //3 4 5
        //6 7 8
        //0 3 6
        //1 4 7
        //2 5 8
        //0 4 8
        //2 4 6

        //Se crea un array con todas las posibles posiciones de victoria
        var ganador = arrayOf(
            intArrayOf(0,1,2),intArrayOf(3,4,5),intArrayOf(6,7,8),intArrayOf(0,3,6),
            intArrayOf(1,4,7),intArrayOf(2,5,8),intArrayOf(0,4,8),intArrayOf(2,4,6)
        )

        //Se recorren los 8 elementos del array en busca de que exista alguna posicion ganadora. Para
        //esto, se crea la variable val0, val1 y val2, en las cuales se almacenaran las posiciones de
        //cada elemento del array.

        //POR EJEMPLO: ganador[i][0] con i = 0 es el primer elemento del array "ganador" y la posicion
        //0 de ese primer elemento. El primer elemento del array "ganador" es el array [0,1,2], y la
        //posicion 0 de ese primer elemento es 0. Entonces, este valor se almacena en la variable
        //val0. Luego sigue el elemento ganador[i][1], cuyo valor es 1 y despues el elemento
        //ganador[i][2], cuyo valor es 2.

        for(i in 0 until ganador.size) {
            var val0 = ganador[i][0]
            var val1 = ganador[i][1]
            var val2 = ganador[i][2]

            //Como hicimos que en el array bloqBtn se almacene el numero 0 o 1 dependiendo del
            //jugador que ha tenido su turno, se verifica que en cualquiera de esas posiciones de
            //victoria este el numero del jugador.

            //POR EJEMPLO: En caso de que el jugador 1 haya ganado, se verifica que los valores de
            //val0, val1 y val2 sean todos iguales a 0, pues es el numero que le corresponde al
            //jugador 1. Y si las variables son iguales a 1, entonces el jugador 2 es el ganador.
            if(bloqBtn[val0] == bloqBtn[val1] && bloqBtn[val1] == bloqBtn[val2]) {

                //Pero en caso de que los valores comparados sean iguales a -1, significaria que todavia
                //no se ha jugado tal casilla. Entonces, antes de esto, verificamos que la casilla/boton
                //haya sido jugado/presionado
                if(bloqBtn[val0] != -1){
                    //Cuando se tenga que el valor de la posicion de bloqBtn no sea -1, significa que
                    //se ha jugado, entonces el juego ya no estara activo.
                    juegoActivo = false

                    //Luego, se comprueba a qué jugador le corresponde la victoria
                    //jugador1 = 0, si val0 es igual a 0, significa que ha ganado ese jugador.
                    if(bloqBtn[val0] == jugador1) {
                        //Se muestra un mensaje como pop-up indicando la victoria. Para eso, se llama
                        //a la funcion mostrarMensaje() que requere de un mensaje que almacenaremos
                        //en la variable s
                        mostrarMensaje("¡El jugador O gana!")
                        //turno.setText("¡El jugador 1 gana!")
                    }else{
                        //Caso contrario, ha ganado el jugador 2
                        mostrarMensaje("¡El jugador X gana!")
                        //turno.setText("¡El jugador 2 gana!")
                    }
                }
                return
            }
        }

        //Por si hay un empate, se define un contador
        var cont = 0

        //Al finalizar el juego, si en el array de botones bloqueados/jugados bloqBtn se tiene que
        //algun boton tiene el valor de -1, significa que tal boton no se ha jugado, por lo cual
        //aumentamos en 1 al contador
        for(i in 0 until bloqBtn.size) {
            if(bloqBtn[i] == -1) {
                cont++
            }
        }

        //Si al final del juego, se tiene que el contador esta en 0, significa que si se han jugado
        //todos los botones, pero como ya se pasó la comprobación de victoria y no se ha llegado a
        //ningun resultado, entonces se ha llegado a un empate
        if(cont == 0) {
            //Se llama a la funcion mostrarMensaje() para indicar que es un empate
            mostrarMensaje("Es un empate")
            return
        }
    }

    //La funcion mostrarMensaje() se encarga de mostrar un pop-up (AlertDialog) con un mensaje para
    //el jugador, que puede ser de victoria o empate
    @RequiresApi(Build.VERSION_CODES.M)
    private fun mostrarMensaje(s: String) {
        //Construimos el AlertDialog
        AlertDialog.Builder(this)
            //Se muestra el mensaje que se almaceno en la variable s
            .setMessage(s)
            //Titulo del mensaje
            .setTitle("TicTacToe")
            //Creamos un boton que permitira reiniciar el juego, este llamará a la funcion reiniciarJuego()
            .setPositiveButton("Reiniciar juego", DialogInterface.OnClickListener { dialogInterface, i ->
                reiniciarJuego()
            })
            //Mostramos el mensaje
            .show()
    }

    //Creamos la funcion reinicarJuego()
    @RequiresApi(Build.VERSION_CODES.M)
    private fun reiniciarJuego() {

        //Reiniciamos las variables a su estado original
        bloqBtn = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1)
        jugadorAct = jugador1
        juegoActivo = true
        turno.setText("Turno del jugador 1")

        //Limpiamos el texto de los botones
        a1.setText("")
        a2.setText("")
        a3.setText("")
        b1.setText("")
        b2.setText("")
        b3.setText("")
        c1.setText("")
        c2.setText("")
        c3.setText("")

        //Reiniciamos el color original de los botones
        a1.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        a2.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        a3.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        b1.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        b2.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        b3.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        c1.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        c2.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
        c3.backgroundTintList = getColorStateList(com.google.android.material.R.color.design_default_color_primary)
    }
}