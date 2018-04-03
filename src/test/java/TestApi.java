import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestApi {

    @Autowired
    private MockMvc mvc;

    //@MockBean
    //private UserVehicleService userVehicleService;

    @Test
    public void testExample() throws Exception {
       // given(this.userVehicleService.getVehicleDetails("sboot"))
       //         .willReturn(new VehicleDetails("Honda", "Civic"));
       // this.mvc.perform(get("/sboot/vehicle").accept(MediaType.TEXT_PLAIN))
          //      .andExpect(status().isOk()).andExpect(content().string("Honda Civic"));
    }
}