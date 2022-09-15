package gr.apt.lms;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
@PWA(name = "Leave Management System", shortName = "LMS")
public class Main implements AppShellConfigurator {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}
