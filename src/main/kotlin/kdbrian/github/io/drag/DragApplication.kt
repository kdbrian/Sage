package kdbrian.github.io.drag

import graphql.scalars.ExtendedScalars
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableScheduling
class DragApplication{

	@Bean
	fun runtimeWiringConfigurer(): RuntimeWiringConfigurer = RuntimeWiringConfigurer { builder ->
		builder
			.scalar(ExtendedScalars.Object)
	}
}

fun main(args: Array<String>) {
	runApplication<DragApplication>(*args)
}
