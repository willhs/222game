package game.ui.window;


public class WindowThread extends Thread {
	private final int delay; // delay between pulses
	private final GameWindow display;

	public WindowThread(int delay, GameWindow display) {
		this.delay = delay;
		this.display = display;
	}

	public void run() {
		while(true) {
			// Loop forever
			try {
				Thread.sleep(delay);
				if(display != null) {
					display.repaint();
				}
			} catch(InterruptedException e) {
				// should never happen
			}
		}
	}
}

