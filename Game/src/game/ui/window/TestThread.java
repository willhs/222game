package game.ui.window;

/**
 * @Stolen from packman
 * */
public class TestThread extends Thread {
	private final int delay; // delay between pulses in us
	private final GameWindow display;

	public TestThread(int delay, GameWindow display) {
		this.delay = delay;
		this.display = display;
	}

	public void run() {
		while(1 == 1) {
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

