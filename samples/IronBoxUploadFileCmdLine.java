/**
 *	Demonstrates how to upload a file to an IronBox secure
 *	package usinig command line parameters:
 *
 * 	Compile:
 * 		javac -cp ".;IronBoxREST.jar" IronBoxUploadFileCmdLine.java
 *
 * 	Usage:
 *		java IronBoxUploadFileCmdLine <file_path_to_upload>
 *
 * 	Note: IronBox secure containers use AES 256-bit 
 * 	encryption keys, therefore you will neeed to install
 * 	the Java Cryptography Extension (JCE) Unlimited 
 * 	Strength Jurisdiction Policy Files for your Java runtime
 * 	version. By default, JDK and JRE only allows up to 
 * 	AES 128-bit	encryption, the new policy files remove 
 * 	this restriction.
 *
 * 	Note: The IronBox REST jar file is required. You can 
 * 	attain a copy of the source and compile a jar from:
 *
 * 	https://github.com/IronBox/ironbox-client-java
 * 
 *	Written by: Kevin Lam, kevinlam@goironbox.com
 *	Website: www.goironbox.com
 *
 */
import com.goironbox.client.*;
import java.io.*;

public class IronBoxUploadFileCmdLine {

	private static ApiVersion API_VERSION = ApiVersion.LATEST;	
	private static boolean verifySSLCert = true;
	private static boolean verbose = true;
	
	// Replace credentials with your own IronBox credentials, and
	// the target container ID you are trying to upload to
	private static String email = "username@email.com";
	private static String password = "<Your IronBox password>";
	private static long containerID = 123456;

	public static void main(String args[]) throws Exception {
	
		try {

			// Check that a file was specified
			if (args.length < 1) {
				throw new Exception("Usage: IronBoxUploadFileCmdLine <file_to_upload>");
			}
			String filePath = args[0];

			// Create a new IronBox client object and set
			// any optional parameters
			IronBoxClient ibc = new IronBoxClient(
            	email, password, EntityType.EMAIL_ADDRESS,
            	API_VERSION, ContentFormat.JSON,
            	verbose, verifySSLCert
			);

			// Optional, you can check to see that the API server is
			// responding before execution
			if (!ibc.ping()) {
				throw new Exception("API server not responding");	
			}
			else {
				System.out.println("+ API server is responding");	
			}
				
			// Server is responding, uploading the file, the 
			// IronBox REST client will automatically handle 
			// all data encryption and key handling
			System.out.println("+ Opening file " + filePath);
			File f = new File(filePath); 
			String fileName = f.getName();
			if (!ibc.uploadFileToContainer(containerID,f,fileName)) {
				throw new Exception("Unable to upload file");
			}
			else {
				System.out.println("+ File uploaded successfully");
			}
		}
		catch (Exception e) {
			
			// Error encountered
			System.out.println("- " + e.getMessage());
		}
	}
}

