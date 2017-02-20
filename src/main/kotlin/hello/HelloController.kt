package hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HelloController() {

	@GetMapping("/hello")
	fun helloworld() = "index"
}
