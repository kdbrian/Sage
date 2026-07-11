package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.data.impl.ReportService
import kdbrian.github.io.drag.domain.dto.ReportSummary
import kdbrian.github.io.drag.domain.dto.TopicReport
import kdbrian.github.io.drag.domain.dto.UploaderReport
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportService: ReportService,
) {

    @GetMapping("/summary")
    fun summary(): ReportSummary = reportService.summary()

    @GetMapping("/topics")
    fun byTopic(): List<TopicReport> = reportService.byTopic()

    @GetMapping("/uploads")
    fun byUploader(): List<UploaderReport> = reportService.byUploader()
}
