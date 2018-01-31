package mqtt.utilities;

import java.util.ArrayList;

/**
 * Created by harry on 08/12/2017.
 */

public class BlockingQueue<Item> {
    private ArrayList<Item> queue = new ArrayList<>();

    public synchronized void put(Item item) {
        queue.add(item);
        notifyAll();
    }

    public synchronized Item take() {
        while(queue.isEmpty()) {
            try {
                wait();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        return queue.remove(0);
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // this is not safe
    public synchronized Item peek() {
        while(queue.isEmpty()) {
            try {
                wait();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        return queue.get(0);
    }
}
