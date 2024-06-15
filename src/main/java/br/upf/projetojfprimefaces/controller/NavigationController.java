import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class NavigationController implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getCurrentPage() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        String currentPage = viewId.substring(viewId.lastIndexOf("/") + 1, viewId.lastIndexOf("."));
        System.out.println("Current Page: " + currentPage);  // Print the current page to the console
        return currentPage;
    }

    public boolean isActive(String page) {
        return getCurrentPage().equals(page);
    }
}