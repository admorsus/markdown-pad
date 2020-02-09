package markdown;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;

@SuppressWarnings({ "serial", "unused" })

public class MarkdownEditor extends JEditorPane {

	/*
	 * viewMode is the display state for the editor it can be set to "html", "raw",
	 * "display"
	 */
	private String viewMode;
	private String rawContent;

	private Parser markdownParser;
	private HtmlRenderer htmlRenderer;
	private MutableDataSet markdownOptions;

	public MarkdownEditor() {
		super();
		viewMode = "raw";
	}

	private void store() {
		if (viewMode == "raw") {
			rawContent = getText();
		}
	}

	public void showRawView() {
		store();
		viewMode = "raw";
		setContentType("text/plain");
		setEditable(true);
	}

	public void showDisplayView() {
		store();
		viewMode = "display";

	}

	public void showHtmlCodeView() {
		viewMode = "html";

	}
}
