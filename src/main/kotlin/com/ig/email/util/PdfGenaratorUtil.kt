package com.ig.email.util

import org.dom4j.DocumentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Component
class PdfGenaratorUtil {
    @Autowired
    private val templateEngine: TemplateEngine? = null

    @Throws(IOException::class, DocumentException::class)
    fun createPdf(templatename: String?, map: Map<*, *>?): String? {
        var fileNameUrl = ""
        val ctx = Context()
        if (map != null) {
            val itMap: Iterator<*> = map.entries.iterator()
            while (itMap.hasNext()) {
                val (key, value) = itMap.next() as Map.Entry<*, *>
                ctx.setVariable(key.toString(), value)
            }
        }
        val processedHtml = templateEngine!!.process(templatename, ctx)
        var os: FileOutputStream? = null
        val studentId = map!!["ID"].toString()
        try {
            val outputFile = File.createTempFile("user_" + studentId + "_", ".pdf")
            os = FileOutputStream(outputFile)
            val itr = ITextRenderer()
            itr.setDocumentFromString(processedHtml)
            itr.layout()
            itr.createPDF(os, false)
            itr.finishPDF()
            fileNameUrl = outputFile.name
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                }
            }
        }
        return fileNameUrl
    }
}