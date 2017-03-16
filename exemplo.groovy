if (params.rowsCount.toInteger() == 1) {
                def f = new File(xmlPath + params.nome.toString())
                if(f.exists()) {
                    bytes = f.getBytes()
                    response.setHeader("Content-disposition", "attachment;filename=${params.nome.toString()}")
                }
            } else {
                def filename = System.currentTimeMillis() + ".zip"
                def destination = xmlPath + filename
                def zipOutput = new ZipOutputStream(new FileOutputStream(destination))
                params.nome.each({ file ->
                    def f = new File(xmlPath + file)
                    def zipEntry = new ZipEntry(file)
                    zipEntry.time = f.lastModified()
                    zipOutput.putNextEntry(zipEntry)
                    if( f.isFile() ){
                        zipOutput << new FileInputStream(f)
                    }
                })
                zipOutput.close()
                bytes = new File(destination).getBytes()
                response.setHeader("Content-disposition", "attachment;filename=$filename")
            }
            response.setContentType("application/octet-stream")
            response.outputStream << bytes
