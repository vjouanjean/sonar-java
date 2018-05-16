import java.io.IOException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class S2078 extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static String getTheParameter(String p, HttpServletRequest request) {
    return request.getParameter(p);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
    doOtherPost(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String ldabSubcontext = getTheParameter("ldabSubcontext", request);
    if (ldabSubcontext == null) {
      ldabSubcontext = "default";
    }

    try {
      Context context = getContext();
      // executing LDAP instruction without verification
      context.createSubcontext(ldabSubcontext); // Noncompliant
    } catch (NamingException e) {
      throw new ServletException(String.format("Unable to create LDAP sub-context %s.", ldabSubcontext));
    }
  }

  public void doOtherPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String searchQuery = getTheParameter("searchQuery", request);
    if (searchQuery == null) {
      searchQuery = "";
    }

    try {
      DirContext context = getContext();
      // executing LDAP instruction without verification
      context.search(searchQuery, "myfilter", new SearchControls()); // Noncompliant
    } catch (NamingException e) {
      throw new ServletException(String.format("Unable to search in LDAP context %s.", searchQuery));
    }

  }

  private DirContext getContext() throws NamingException {
    return new InitialDirContext();
  }
}
