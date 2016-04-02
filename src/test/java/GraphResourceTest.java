
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import no.dblp.rest.GraphResource;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Test;

public class GraphResourceTest {

    @Test
    public void test() throws Exception {

        Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();

        POJOResourceFactory noDefaults = new POJOResourceFactory(GraphResource.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);

        MockHttpRequest request = MockHttpRequest.get(URLEncoder.encode("http://localhost:8080/DBLPGraph/webresources/Graph/Jan Arne Telle", "UTF-8"));
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("basic", response.getContentAsString());

    }

}
