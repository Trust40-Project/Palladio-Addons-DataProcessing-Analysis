package pcm.dataprocessing.analysis.launcher.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import pcm.dataprocessing.analysis.launcher.constants.Constants;

/**
 * Adjudsts a given launch configuration with analysis goal settings.
 * 
 * @author Mirko Sowa
 *
 */
public class AdvancedConfigurationTab extends AbstractLaunchConfigurationTab {

	private Composite comp;
	private Group argAndReturnIndexing;
	private Group optimNegations;
	private Group shortAssign;

	private Button[] argAndReturnRadio = new Button[2];
	private Button[] optimNegationRadio = new Button[2];
	private Button[] shortAssignRadio = new Button[2];

	@Override
	public void createControl(Composite parent) {

		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));

		argAndReturnIndexing = new Group(comp, SWT.BORDER);
		argAndReturnIndexing.setText(Constants.ADV_ARG_AND_RETURN.getConstant());
		argAndReturnIndexing.setLayout(new RowLayout(SWT.HORIZONTAL));

		argAndReturnRadio[0] = new Button(argAndReturnIndexing, SWT.RADIO);
		argAndReturnRadio[0].setText("False");
		argAndReturnRadio[1] = new Button(argAndReturnIndexing, SWT.RADIO);
		argAndReturnRadio[1].setText("True");

		optimNegations = new Group(comp, SWT.BORDER);
		optimNegations.setText(Constants.ADV_OPTIM_NEGATION.getConstant());
		optimNegations.setLayout(new RowLayout(SWT.HORIZONTAL));

		optimNegationRadio[0] = new Button(optimNegations, SWT.RADIO);
		optimNegationRadio[0].setText("False");
		optimNegationRadio[1] = new Button(optimNegations, SWT.RADIO);
		optimNegationRadio[1].setText("True");

		shortAssign = new Group(comp, SWT.BORDER);
		shortAssign.setText(Constants.ADV_SHORT_ASSIGN.getConstant());
		shortAssign.setLayout(new RowLayout(SWT.HORIZONTAL));

		shortAssignRadio[0] = new Button(optimNegations, SWT.RADIO);
		shortAssignRadio[0].setText("False");
		shortAssignRadio[1] = new Button(optimNegations, SWT.RADIO);
		shortAssignRadio[1].setText("True");

		setControl(comp);

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.ADV_ARG_AND_RETURN.getConstant(), false);
		configuration.setAttribute(Constants.ADV_OPTIM_NEGATION.getConstant(), false);
		configuration.setAttribute(Constants.ADV_SHORT_ASSIGN.getConstant(), false);

	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		setTrueFalse(argAndReturnRadio, false);
		setTrueFalse(optimNegationRadio, false);
		setTrueFalse(shortAssignRadio, false);
		try {
			setTrueFalse(argAndReturnRadio,
					configuration.getAttribute(Constants.ADV_ARG_AND_RETURN.getConstant(), false));
			setTrueFalse(optimNegationRadio,
					configuration.getAttribute(Constants.ADV_OPTIM_NEGATION.getConstant(), false));
			setTrueFalse(shortAssignRadio, configuration.getAttribute(Constants.ADV_SHORT_ASSIGN.getConstant(), false));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.ADV_ARG_AND_RETURN.getConstant(), getTrueFalse(argAndReturnRadio));
		configuration.setAttribute(Constants.ADV_OPTIM_NEGATION.getConstant(), getTrueFalse(optimNegationRadio));
		configuration.setAttribute(Constants.ADV_SHORT_ASSIGN.getConstant(), getTrueFalse(shortAssignRadio));
	}

	@Override
	public String getName() {
		// TODO remove hardcoded
		return "test";
	}

	/**
	 * 
	 * @return
	 */
	private boolean getTrueFalse(Button[] array) {
		if (array.length <= 2) {
			return array[1].getSelection();
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	private void setTrueFalse(Button[] array, boolean value) {
		if (array.length <= 2) {
			if (value) {
				array[0].setSelection(false);
				array[1].setSelection(true);

			} else {
				array[1].setSelection(false);
				array[0].setSelection(true);
			}
		}
	}
}
