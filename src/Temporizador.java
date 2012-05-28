import javax.swing.JLabel;



public class Temporizador extends Thread{

	int segundos = 0;
	boolean salir = false;
	JLabel txtTiempo;
	public Temporizador( JLabel txt ){
	txtTiempo = txt;
	}
	public void run() {
		
		while( !salir ) {
			txtTiempo.setText(""+segundos);
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		segundos++;
		}
		
	}
	public void parar (boolean para){
		salir = ( para ) ? true : false;
		
	}
	
}
