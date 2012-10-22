package edu.uc.labs.wirelessprinting.mail2print;

import com.typesafe.config.Config;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Matt Silbernagel
 */
public class Main {
    
    final private Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {                
        
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        context.registerShutdownHook();

        Config config = (Config) context.getBean("config");                        
        System.out.println(config.getString("application.name"));
        
        /*
        Properties props = System.getProperties();
        
        Session s = Session.getInstance(props, null);
        //s.setDebug(true);        

        store = s.getStore("imaps");
        store.connect("ucmail.uc.edu", 993, "silbermm", "Othello2!");
        folder = store.getFolder("INBOX");        
        folder.open(Folder.READ_WRITE);
        //Message message[] = folder.getMessages();
        ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

        Runnable r = new Runnable() {

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Message messages[] = folder.search(ft);
                        if (messages.length > 0) {
                            for (Message m : messages) {
                                javax.mail.Address[] from = m.getFrom();                                
                                System.out.println(from[0]);
                                System.out.println(m.getSubject());
                                
                            }
                        }
                    } catch (MessagingException e) {
                        System.out.println("MessagingException: " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
                try {
                    folder.close(false);
                    store.close();
                } catch (MessagingException e) {
                    System.out.println("MessagingException: " + e.getMessage());
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
        * 
        */

    }
}
