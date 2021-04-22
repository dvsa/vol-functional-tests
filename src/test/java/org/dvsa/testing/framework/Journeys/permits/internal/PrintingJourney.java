package org.dvsa.testing.framework.Journeys.permits.internal;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.lib.pages.enums.AdminOption;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.internal.NavigationBar;
import org.dvsa.testing.lib.pages.internal.printing.BasePrintingPage;
import org.dvsa.testing.lib.pages.internal.printing.PrintIrhpPermitsPage;

import java.net.MalformedURLException;

public class PrintingJourney {

    private static volatile PrintingJourney instance = null;

    protected PrintingJourney(){
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static PrintingJourney getInstance(){
        if (instance == null) {
            synchronized (PrintingJourney.class){
                instance = new PrintingJourney();
            }
        }

        return instance;
    }

    public PrintingJourney getToPrintersPage() throws ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, IllegalBrowserException {
        NavigationBar.administratorButton();
        NavigationBar.administratorList(AdminOption.PRINTING);

        return this;
    }

    public PrintingJourney goPrintIrhpPermits() throws MalformedURLException, IllegalBrowserException {
        BasePrintingPage.Sidebar.select(BasePrintingPage.Section.PrintIrhpPermits);
        return this;
    }

    public PrintingJourney tickAllApplicationRefs(String... references) throws MalformedURLException, IllegalBrowserException {
        PrintIrhpPermitsPage.tick(PrintIrhpPermitsPage.By.ApplicationReference, references);
        return this;
    }

    public PrintingJourney stage() throws MalformedURLException, IllegalBrowserException {
        PrintIrhpPermitsPage.continueButton();
        return this;
    }

    public PrintingJourney print() throws MalformedURLException, IllegalBrowserException {
        PrintIrhpPermitsPage.confirm();
        return this;
    }

    public PrintingJourney printIrhpPermit(String... references) throws ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, IllegalBrowserException {
        getInstance().getToPrintersPage().goPrintIrhpPermits().tickAllApplicationRefs(references).stage().print();
        return this;
    }

}
