package markdown;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

@SuppressWarnings("serial")

/**
 * WYSIWYG markdown editor desktop application.
 * 
 * Licensed under MIT, see LICENSE file
 * 
 * @author admorsus - Miguel Bautista Pérez
 */

public class MarkdownEditor extends JFrame {

	private String content;
	private String displayState;
	private JEditorPane editor;
	private JFileChooser fileChooser = new JFileChooser();

	private MutableDataSet markdownOptions = new MutableDataSet();
	private Parser markdownParser;
	private HtmlRenderer htmlRenderer;

	private Action liveViewAction;
	private Action htmlViewAction;
	private Action textViewAction;

	private Action openAction = new OpenAction();
	private Action saveAction = new SaveAction();
	private Action exitAction = new ExitAction();

	public MarkdownEditor() {
		displayState = "text";

		// Flexmark Objects //
		markdownParser = Parser.builder(markdownOptions).build();
		htmlRenderer = HtmlRenderer.builder(markdownOptions).build();

		// UI Making //
		setTitle("Markdown Editor");
		editor = new JEditorPane();
		editor.setFont(new Font("DialogInput", Font.PLAIN, 14));
		editor.setPreferredSize(new Dimension(350, 250));
		editor.setContentType("text/plain");
		getContentPane().add(editor, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel, BorderLayout.NORTH);

		// File menu //
		JPopupMenu fileMenu = new JPopupMenu();
		addPopup(editor, fileMenu); //
		fileMenu.add(openAction);
		fileMenu.add(saveAction);
		fileMenu.addSeparator();
		fileMenu.add(exitAction);
		MenuButton btnMenu = new MenuButton("File", fileMenu);
		panel.add(btnMenu);

		// Display state radio-buttons //
		JRadioButton btnText = new JRadioButton("Edit");
		btnText.setOpaque(false);
		panel.add(btnText);
		JRadioButton btnMarkdown = new JRadioButton("Markdown");
		btnMarkdown.setOpaque(false);
		panel.add(btnMarkdown);
		JRadioButton btnHtml = new JRadioButton("HTML");
		btnHtml.setOpaque(false);
		panel.add(btnHtml);
		btnText.setSelected(true);

		ButtonGroup viewModeBtnGroup = new ButtonGroup();
		viewModeBtnGroup.add(btnText);
		viewModeBtnGroup.add(btnMarkdown);
		viewModeBtnGroup.add(btnHtml);

		// Action declarations //
		liveViewAction = new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if (setDisplayState("md")) {
					String html = renderHtml(content);
					editor.setEditable(false);
					editor.setText(html);
				}
			}
		};

		htmlViewAction = new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if (setDisplayState("html")) {
					String html = renderHtml(content);
					editor.setEditable(false);
					editor.setText(html);
				}
			}
		};

		textViewAction = new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if (setDisplayState("text")) {
					editor.setEditable(true);
					editor.setText(content);
				}
			}
		};

		// Load window //
		btnText.addActionListener(textViewAction);
		btnMarkdown.addActionListener(liveViewAction);
		btnHtml.addActionListener(htmlViewAction);
		pack();
		editor.requestFocusInWindow();
		setVisible(true);
	}

	/**
	 * Creates the html document used to give format to the given markdown code
	 * 
	 * @param markdown code string to be rendered
	 * @return html string
	 */
	public String renderHtml(String markdown) {
		Node document = markdownParser.parse(markdown);
		return htmlRenderer.render(document);
	}

	/**
	 * stores the content for further processing
	 */
	public void keepContent() {
		content = editor.getText();
	}

	/**
	 * Action to show a File Chooser dialog and open the selected file
	 * 
	 * @author Fred Swartz
	 */
	class OpenAction extends AbstractAction {
		public OpenAction() {
			super("Open...");
			// putValue(MNEMONIC_KEY, new Integer('O'));
		}

		public void actionPerformed(ActionEvent e) {
			int retval = fileChooser.showOpenDialog(MarkdownEditor.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				try {
					setDisplayState("text");
					FileReader reader = new FileReader(f);
					editor.read(reader, ""); // Use TextComponent read
				} catch (IOException ioex) {
					System.out.println(e);
					System.exit(1);
				}
			}
			keepContent();
		}
	}

	/**
	 * Action to show a File Chooser dialog and save the edited file
	 * 
	 * @author Fred Swartz
	 */
	class SaveAction extends AbstractAction {
		SaveAction() {
			super("Save...");
			// putValue(MNEMONIC_KEY, new Integer('S'));
		}

		public void actionPerformed(ActionEvent e) {
			int retval = fileChooser.showSaveDialog(MarkdownEditor.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				try {
					FileWriter writer = new FileWriter(f);
					editor.write(writer); // Use TextComponent write
				} catch (IOException ioex) {
					JOptionPane.showMessageDialog(MarkdownEditor.this, ioex);
					System.exit(1);
				}
			}
		}
	}

	/**
	 * Action used to exit the program
	 */
	class ExitAction extends AbstractAction {

		public ExitAction() {
			super("Exit");
			// putValue(MNEMONIC_KEY, new Integer('X'));
		}

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Sets the display state for the JEditorPane.
	 * 
	 * @param state to be set, which can be {@code text | md | html}
	 * @return true if {@code state} is different than the previous state.
	 */
	public boolean setDisplayState(String state) {
		if (state.equals(displayState)) {
			return false;
			// if verbosity syso state not changed
		}
		if (displayState.equals("text")) {
			keepContent();
		}
		displayState = state;

		String contentType = "text/";
		if (state.equals("md")) {
			contentType += "html";
		} else {
			contentType += "plain";
		}
		editor.setContentType(contentType);

		return true;

	}

	/**
	 * Main method that just creates a new instance of the app
	 */
	public static void main(String[] args) {
		new MarkdownEditor();
	}

	/**
	 * Makes a menu able to be opened by right click
	 * 
	 * @param component where to right click
	 * @param popup     menu to be opened
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
