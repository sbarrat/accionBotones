

public class Temporizador extends Thread{

	int segundos = 0;
	accionBotones ventana;
	
	public Temporizador( accionBotones j ){
	ventana = (accionBotones) j;	
	}
	public void tiempo( ) {
		while(segundos != 120 ) {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		segundos++;
		ventana.txtTiempo.setText(""+segundos+"");
		System.out.println(segundos);
		}
	}
}
