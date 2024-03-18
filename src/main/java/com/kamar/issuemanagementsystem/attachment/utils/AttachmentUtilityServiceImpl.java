/*
package com.kamar.issuemanagementsystem.attachment.utils;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

*/
/**
 * implementation of the attachments utility contract.
 * @author kamar baraka.*//*


@Service
@RequiredArgsConstructor
public class AttachmentUtilityServiceImpl implements AttachmentUtilityService {

    private final TicketUtilities ticketUtilities;

    @Override
    public File convertAttachmentToFile(Attachment attachment) throws IOException {

        */
/*create a temporary file*//*

        File attachmentFile = File.createTempFile("attachments", attachment.getFilename());

        */
/*get the attachments content*//*

        byte[] content = ticketUtilities.convertBlobToBytes(attachment.getContent());

        */
/*write the content to file*//*

        try (FileOutputStream fileOutputStream = new FileOutputStream(attachmentFile)) {

            fileOutputStream.write(content);
        }

        return attachmentFile;
    }

    @Override
    public File compressFilesToZip(List<File> files, String zipFileName) throws Exception {

        */
/*create the zip file*//*

        File zipFile = File.createTempFile(zipFileName, ".zip");
        */
/*create a file out stream to read the zip file into the zos*//*

        FileOutputStream zipInStream = new FileOutputStream(zipFile);
        */
/*create a zip output stream to write the zip file*//*


        try (ZipOutputStream zipOutStream = new ZipOutputStream(zipInStream)) {

            */
/*add files to zip*//*

            for (File file : files) {

                */
/*create a file input stream to read the file*//*


                try (FileInputStream inputStream = new FileInputStream(file);) {

                    */
/*create a zip entry with the name of the file*//*

                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutStream.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0){

                        */
/*write to zip*//*

                        zipOutStream.write(buffer, 0, len);
                    }
                }
            }
        }

        return zipFile;
    }
}
*/
