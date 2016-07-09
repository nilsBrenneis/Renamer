package renamer;
class UnevenCountException extends Exception {

	private static final long serialVersionUID = 1L;

	UnevenCountException() {
		super(
				"Die Anzahl an Dateien im Ordner ist ungleich der Anzahl der Dateinamen.");
	}
}