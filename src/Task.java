/**
 * Created by sam on 1/2/17.
 */
public class Task implements Runnable {
    private int number;
    public Task(int num){
        number = num;
    }
    @Override
    public void run() {
        System.out.println("Task number # = " + number + " is running !");
    }
}
