package xyz.stasiak.cobudget;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.SystemContextView;

import java.io.File;

public class StructurizrMain {

    public static void main(String[] args) throws Exception {
        var workspace = new Workspace("CoBudget", "Model systemu");
        var model = workspace.getModel();
        model.setEnterprise(new Enterprise("CoBudget"));

        Person customer = model.addPerson(
                Location.External,
                "Użytkownik",
                "Użytkownik systemu, tworzy i planuje budżet domowy"
        );

        SoftwareSystem budgetSystem = model.addSoftwareSystem(
                Location.Internal,
                "Planowanie budżetu online",
                "Pozwala na zarządzanie swoim budżetem"
        );

        SoftwareSystem authSystem = model.addSoftwareSystem(
                "Okta",
                "System do autentykacji i autoryzacji użytkowników"
        );

        customer.uses(budgetSystem, "Używa");
        customer.uses(authSystem, "Używa", "JSON/HTTPS");
        budgetSystem.uses(authSystem, "Autoryzuje użytkownika", "OAuth2/OpenID Connect");

        Container webApp = budgetSystem.addContainer(
                "Aplikacja webowa",
                "Umożliwia korzystanie z systemu przez przeglądarkę internetową",
                "VueJS"
        );

        Container apiApp = budgetSystem.addContainer(
                "API",
                "Umożliwia zarządzanie budżetem poprzez JSON/HTTPS API.",
                "Java, Spring"
        );

        Container database = budgetSystem.addContainer(
                "Baza danych",
                "Zawiera informacje o budżetach, wydatkach oraz konfiguracji",
                "PostgreSQL"
        );

        customer.uses(webApp, "Używa", "Przeglądarka internetowa");
        webApp.uses(apiApp, "Używa", "JSON/HTTPS");
        webApp.uses(authSystem, "Autoryzuje", "OAuth2/OpenID Connect");
        apiApp.uses(database, "Czyta/Zapisuje", "JDBC");
        apiApp.uses(authSystem, "Autoryzuje", "OAuth2/OpenID Connect");

        Component entryController = apiApp.addComponent(
                "Wydatki",
                "Pozwala na zarządzanie wydatkami",
                "Spring MVC Rest Controller"
        );

        Component entryQueryController = apiApp.addComponent(
                "Odczyt wydatków",
                "Pozwala na odczytywanie wydatków w danym okresie",
                "Spring MVC Rest Controller"
        );

        Component categoryController = apiApp.addComponent(
                "Kategorie",
                "Pozwala na zarządzanie dostępnymi kategoriami w budżecie",
                "Spring MVC Rest Controller"
        );

        Component categoryQueryController = apiApp.addComponent(
                "Odczyt kategorii",
                "Pozwala na odczyt utworzonych kategorii i podkategorii",
                "Spring MVC Rest Controller"
        );

        Component oktaOAuth2Client = apiApp.addComponent(
                "Autoryzacja",
                "Autoryzuje użytkownika dając mu dostęp do zasobów",
                "Okta Spring Boot Starter"
        );

        webApp.uses(entryController, "Używa", "JSON/HTTPS");
        webApp.uses(entryQueryController, "Używa", "JSON/HTTPS");
        webApp.uses(categoryController, "Używa", "JSON/HTTPS");
        webApp.uses(categoryQueryController, "Używa", "JSON/HTTPS");
        entryController.uses(database, "Zapisuje", "JDBC");
        entryQueryController.uses(database, "Odczytuje", "JDBC");
        categoryController.uses(database, "Zapisuje", "JDBC");
        categoryQueryController.uses(database, "Odczytuje", "JDBC");
        oktaOAuth2Client.uses(authSystem, "Autoryzuje", "OAuth2/OpenID Connect");

        var views = workspace.getViews();
        SystemContextView systemContextView = views.createSystemContextView(
                budgetSystem,
                "SystemContext",
                "Diagram kontekstowy systemu do planowania budżetu"
        );
        systemContextView.addNearestNeighbours(budgetSystem);
        systemContextView.addAnimation(customer);
        systemContextView.addAnimation(budgetSystem);
        systemContextView.addAnimation(authSystem);

        ContainerView containerView = views.createContainerView(
                budgetSystem,
                "Containers",
                "Diagram kontenerów systemu do planowania budżetu"
        );

        containerView.add(customer);
        containerView.addAllContainers();
        containerView.add(authSystem);
        containerView.addAnimation(customer, authSystem);
        containerView.addAnimation(webApp);
        containerView.addAnimation(apiApp);
        containerView.addAnimation(database);

        ComponentView componentView = views.createComponentView(
                apiApp,
                "Components",
                "Diagram komponentów aplikacji API"
        );

        componentView.add(webApp);
        componentView.add(database);
        componentView.addAllComponents();
        componentView.add(authSystem);
        componentView.setPaperSize(PaperSize.A5_Landscape);
        componentView.addAnimation(webApp);
        componentView.addAnimation(entryController, entryQueryController, database);
        componentView.addAnimation(categoryController, categoryQueryController, database);
        componentView.addAnimation(oktaOAuth2Client, authSystem);

        var file = new File("./workspace.json");
        if (file.exists()) {
            Workspace liteWorkspace = WorkspaceUtils.loadWorkspaceFromJson(file);
            workspace.getViews().copyLayoutInformationFrom(liteWorkspace.getViews());
        }
        WorkspaceUtils.saveWorkspaceToJson(workspace, file);
    }
}

