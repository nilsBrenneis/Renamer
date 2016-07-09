package renamer;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI {
	private final int LETTER_HEIGHT = 20;
	private final int START_SIZE = 60;
	private final int OFFSET_SIZE = 100;

	private JButton renameBtn;
	private JButton pathBtn;
	private JButton resetBtn;

	private JFrame jf;
	private JTextArea jta = new JTextArea();

	private Umbenenner u;

	public GUI(String name, Umbenenner u) {
		jf = new JFrame(name);
		this.u = u;
	}

	public void initFrame() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JPanel buttonsJP = new JPanel();
		renameBtn = new JButton("Umbenennen");
		pathBtn = new JButton("Öffnen");
		resetBtn = new JButton("Reset");

		renameBtn.setEnabled(false);
		resetBtn.setEnabled(false);

		buttonsJP.add(resetBtn, BorderLayout.WEST);
		buttonsJP.add(renameBtn, BorderLayout.CENTER);
		buttonsJP.add(pathBtn, BorderLayout.EAST);

		JScrollPane scrollPane = new JScrollPane(jta,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jf.setSize(300, START_SIZE);
		jf.add(scrollPane, BorderLayout.CENTER);
		jf.add(buttonsJP, BorderLayout.SOUTH);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void fillTextArea(File[] files) {
		for (int i = 0; i < files.length; i++) {
			jta.append(files[i].getName());
			if (i < files.length - 1) {
				jta.append(System.lineSeparator());
			}
		}
	}

	public void setActionListeners() {
		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				u.setFiles(u.getFilesAsArray(u.getPath()));
				jta.setText("");
				fillTextArea(u.getFiles());
				resizeWindow(u.getFiles().length);
			}
		});
		
		pathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFolder();
			}
		});

		renameBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean textAreaOK = true;
				if (u.containsNotUsableChar(jta.getText())) {
  					JOptionPane.showMessageDialog(jf,
							"Dateinamen dürfen keine Sonderzeichen enthalten",
							"Abbruch", JOptionPane.ERROR_MESSAGE);
  					textAreaOK = false;
				}

				if (textAreaOK) {
					try {
						u.renameFiles(jta.getText());
						u.setFiles(u.getFilesAsArray(u.getPath()));
						jta.setText("");
						fillTextArea(u.getFiles());
						resizeWindow(u.getFiles().length);
					} catch (UnevenCountException ex) {
						JOptionPane.showMessageDialog(jf, ex.getMessage(), "Abbruch", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	private void choosePath() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		File f = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
		}
		u.setPath(f.getAbsolutePath());
	}

	private void resizeWindow(int countLines) {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int vertScreenSize = gd.getDisplayMode().getHeight() - OFFSET_SIZE;
		int frameSize = START_SIZE;

		for (int i = 0; i < countLines && frameSize < vertScreenSize; i++) {
			frameSize += LETTER_HEIGHT;
		}
		jf.setSize(300, frameSize);
	}

	public void openFolder() {
		choosePath();
		u.setFiles(u.getFilesAsArray(u.getPath()));
		fillTextArea(u.getFiles());
		resizeWindow(u.getFiles().length);
		renameBtn.setEnabled(true);
		resetBtn.setEnabled(true);
	}
}
