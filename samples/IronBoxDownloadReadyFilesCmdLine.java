/**
 *	Demonstrates how to upload a file to an IronBox secure
 *	package usinig command line parameters:
 *
 * 	Compile:
 * 		javac -cp ".;IronBoxREST.jar" IronBoxDownloadReadyFilesCmdLine.java
 *
 * 	Usage:
 *		java IronBoxDownloadReadyFilesCmdLine <containerID> <email> <password> 
 *
 * 	Note: IronBox secure containers use AES 256-bit 
 * 	encryption keys, therefore you will need to install
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
 *	Change History:
 *	===============
 *	04/23/15	- Initial release
 *
 */
import com.goironbox.client.*;
import java.io.*;
import java.util.List;

public class IronBoxDownloadReadyFilesCmdLine {

	private static ApiVersion API_VERSION = ApiVersion.LATEST;	
	private static boolean verifySSLCert = true;
	private static boolean verbose = true;

	// These will be picked up as command lines parameters	
	private static String email = "<your_email>";
	private static String password = "<your_password>";
	private static long containerID = 100000;

	public static void main(String args[]) throws Exception {
	
		try {

			// Verify that parameters were specified correctly
			if (args.length < 3) {
				throw new Exception("Usage: IronBoxDownloadReadyFilesCmdLine <containerID> <email> <password>");
			}
			containerID = Long.valueOf(args[0]).longValue();
			email = args[1];
			password = args[2];

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
				
			// Server is responding, read the contents of the container and then
			// download the contents of it locally 
			System.out.println("+ Reading container");
			List<BlobInfo> ReadyFiles = ibc.getContainerBlobInfoListByState(containerID, BlobState.READY);	
			for (BlobInfo blobInfo : ReadyFiles) {
				File f = new File(blobInfo.getBlobName());
				ibc.downloadBlobFromContainer(containerID, blobInfo.getBlobID(), f);	
			}
		}
		catch (Exception e) {
			
			// Error encountered
			System.out.println("- " + e.getMessage());
		}
	}
}

