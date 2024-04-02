import javax.swing.*;

public class MetropolisController {
    MetropolisView view;
    MetropolisModel model;

    public MetropolisController(MetropolisView view, MetropolisModel model) {
        this.view = view;
        this.model = model;
        this.view.addListener(e -> {
            if(e.getSource() == view.addBTN) {
                if(view.metropolisTF.getText().equals("") || view.continentTF.getText().equals("")
                        || view.populationTF.getText().equals(""))
                    return;
                model.addData(view.metropolisTF.getText(),
                        view.continentTF.getText(), view.populationTF.getText());
            } else if(e.getSource() == view.searchBTN) {
                model.getData(view.metropolisTF.getText(),
                        view.continentTF.getText(), view.populationTF.getText(),
                        getOptionForModel(view.populationCB),
                        getOptionForModel(view.matchCB));
            }
        });
    }

    /**
     * Based on the given combobox, returns a boolean value for the data model to use.
     * */
    private boolean getOptionForModel(JComboBox CB) {
        if(CB == view.populationCB) {
            if(CB.getSelectedItem().toString().equals("Population larger than"))
                return true;
            return false;
        } else if(CB == view.matchCB) {
            if(CB.getSelectedItem().toString().equals("Exact match"))
                return true;
            return false;
        }
        return false;
    }
}
