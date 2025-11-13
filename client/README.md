
Assuming that you have [Maven](https://maven.apache.org/install.html) installed, you can run the project out-of-the-box from your terminal via

	mvn -pl client -am javafx:run

from your project root (not from within the `client` folder!).

Starting the client within your IDE (Eclipse/IntelliJ) requires setting up OpenJFX.

First download (and unzip!) an [OpenJFX SDK](https://openjfx.io).
Make sure that the download *matches your Java JDK version*.

Then create a *run configuration* for the `Main` class and add the following *VM* commands (which, in IntelliJ, are hidden by default):

	--module-path="/path/to/javafx-sdk/lib"
	--add-modules=javafx.controls,javafx.fxml,javafx.web

Adjust the module path to *your* local download location and make sure you adapt the path
to the `lib`(!) directory (not just the directory that you unzipped)...

*Tip:* Windows paths are different, they uses `\` as path separator and starts with a drive letter like `C:`.

*Tip:* Make sure not to forget the `/lib` at the end of the path.

*Tip:* Double-check that the path is correct. If you receive abstract errors, like `Module javafx.web not found`
or a segmentation fault, you are likely not pointing to the right folder. Try opening the folder and check that
it contains several .jar files, such as `javafx.controls.jar`.
