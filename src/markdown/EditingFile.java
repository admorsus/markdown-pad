package markdown;

import java.util.Locale;

import javax.swing.text.JTextComponent;

import com.vladsch.flexmark.convert.html.FlexmarkHtmlParser;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.Pair;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.mappers.CharMapper;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.util.sequence.Range;
import com.vladsch.flexmark.util.sequence.ReplacedTextMapper;

@SuppressWarnings("deprecation")

public abstract class EditingFile {
	private static MutableDataSet options;
	private static Parser mdParser;
	private static HtmlRenderer htmlRenderer;
	private static FlexmarkHtmlConverter htmlConverter;

	private JTextComponent editor;
	private Document content;

	static {
		options = new MutableDataSet();

		mdParser = Parser.builder(options).build();
		htmlRenderer = HtmlRenderer.builder(options).build();
		htmlConverter = FlexmarkHtmlConverter.builder(options).build();


	}

	/**
	 * Default constructor. <br>
	 * Creates a empty editing file associated with an editor.
	 * 
	 * @param editor which manipulates the content.
	 */
	public EditingFile(JTextComponent editor) {
		this.editor = editor;
		this.content = null;
	}
	
	public EditingFile(JTextComponent editor, String text) {
		this.editor = editor;
		this.content = new 
	}

	public void setContent() {
		content = mdParser.parse(editor.getText());
	}

	public void setContentFromHtml() {
		content = mdParser.
	}

	public void getContent();

	abstract void generateHtml();

}
