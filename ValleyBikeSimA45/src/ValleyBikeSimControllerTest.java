import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValleyBikeSimControllerTest {

	@Test
	void test() {
		ValleyBikeSimView view = new ValleyBikeSimView();
		ValleyBikeSimModel model = new ValleyBikeSimModel();
		ValleyBikeSimController controller = new ValleyBikeSimController(view, model);
		controller.start();
		
	}

}
