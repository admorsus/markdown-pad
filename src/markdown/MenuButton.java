package markdown;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

@SuppressWarnings("serial")

/**
 * Button which can open a given popup menu
 * 
 * @author luca
 * @author admorsus
 */

public class MenuButton extends JToggleButton {

	private JPopupMenu popup;
	private boolean openState = false;

	/**
	 * Default constructor
	 * 
	 * @param name to be displayed in the button
	 * @param menu menu to be displayed when clicked
	 */
	public MenuButton(String name, JPopupMenu menu) {
		super(name);
		this.popup = menu;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JToggleButton b = MenuButton.this;

				if (!openState) {
					popup.show(b, 0, b.getBounds().height);
					b.setSelected(true);
					openState = true;

				} else {
					popup.setVisible(false);
					b.setSelected(false);
					openState = false;

				}
			}
		});

		popup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				MenuButton.this.setSelected(false);
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
	}
}