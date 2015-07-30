package com.javacodegeeks.enterprise.rest.jersey;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.file.FileInputStream;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/files")
public class fileupload {
	private String storageConnectionString=
	 "DefaultEndpointsProtocol=http;" + 
	    "AccountName=sliittest;" + 
	    "AccountKey=ZniNpVOixoQiRWOVsIpL+iXue7hucxgVaWPf2T/ZSiUR8IUyRBmDZX4epVYH4aQrPCFYTjhvlG7XG/ekJubGzg==";
	
	  @POST
	  @Path("/upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)
	  public  Response mymethod(  
			   @FormDataParam("file") InputStream fileInputStream,
			   @FormDataParam("file") FormDataContentDisposition contentDispositionHeader
			  		) 
	  {
		  String fileName = contentDispositionHeader.getFileName();
		  UploadtoAzureStorage(fileInputStream,fileName);
		  String result="File saved to Azure storage, container name: \"mycontainer\"";
		return Response.status(200).entity(result).build();
	  }

	  
	  public void UploadtoAzureStorage(InputStream fileInputStream,String filename)
	  {
		  try
		  {
		      CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

		      CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		      CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
		    
		      byte[] bytes = IOUtils.toByteArray(fileInputStream);
		      CloudBlockBlob blob = container.getBlockBlobReference(filename);
		  
		      BlobOutputStream blobOutputStream = blob.openOutputStream();
              ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes); 

              int next = inputStream.read();
              while (next != -1) {
                    blobOutputStream.write(next);
                    next = inputStream.read();
              }
              blobOutputStream.close();
		      
		      
		      
//		      File source = new File(filename);
//		      blob.upload(fileInputStream, source.length());
		      
//		      blob.upload(fileInputStream,bytes.length);
		  }
		  catch (Exception e)
		  {
		      e.printStackTrace();
		  }
	  }

}