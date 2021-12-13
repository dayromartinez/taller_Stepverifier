package com.StepVerifier.Spring_Webflux_Test;

import com.StepVerifier.Spring_Webflux_Test.Service.Servicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class SpringWebfluxTestApplicationTests {

	@Autowired
	Servicio servicio;

	@Test
	void testMono() {
		Mono<String> uno = servicio.buscarUno();
		StepVerifier.create(uno).expectNext("Pedro").verifyComplete();
	}

	@Test
	void testVarios() {
		Flux<String> uno = servicio.buscarTodos();
		StepVerifier.create(uno).expectNext("Pedro").expectNext("Maria").expectNext("Jesus").expectNext("Carmen").verifyComplete();
	}

	@Test
	void testVariosLento() {
		Flux<String> uno = servicio.buscarTodosLento();
		StepVerifier.create(uno)
				.expectNext("Pedro")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("Maria")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("Jesus")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("Carmen")
				.thenAwait(Duration.ofSeconds(1)).verifyComplete();
	}

	@Test
	void testTodosFiltro() {
		Flux<String> source = servicio.buscarTodosFiltro();

		StepVerifier
				.create(source)
				.expectNext("JOHN")
				.expectNextMatches(name -> name.startsWith("MA"))
				.expectNext("CLOE", "CATE")
				.expectComplete()
				.verify();
	}

	@Test
	void testAfirmacionPosteriorEjecucion(){

		Flux<Integer> source = servicio.afirmacionPosteriorEjecucion();
		StepVerifier.create(source)
				.expectNext(2)
				.expectComplete()
				.verifyThenAssertThat()
				.hasDropped(4)
				.tookLessThan(Duration.ofMillis(1000));
	}
}
