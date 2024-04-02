public class MetropolisMain {
    public static void main(String[] args) {
        MetropolisModel model = new MetropolisModel();
        MetropolisView view = new MetropolisView(model);
        MetropolisController controller = new MetropolisController(view, model);
    }
}
