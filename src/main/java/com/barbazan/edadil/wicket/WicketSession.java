package com.barbazan.edadil.wicket;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Dmitry Malyshev
 * Date: 26.11.13 Time: 16:55
 * Email: dmitry.malyshev@gmail.com
 */
public class WicketSession extends WebSession {

    private static Logger logger = Logger.getLogger(WicketSession.class);

    private final LinkedList<FeedbackHolder> feedbackHolders = new LinkedList<FeedbackHolder>();

    public static WicketSession get() {
        return (WicketSession) WebSession.get();
    }

    public WicketSession(Request request) {
        super(request);
    }

    private FeedbackHolder getFeedbackHolder() {
        return feedbackHolders.getFirst();
    }

    public Component createFeedbackPanel(String wid) {
        if (feedbackHolders.isEmpty()) {
            return null;
        } else {
            FeedbackHolder holder = getFeedbackHolder();
            try {
                IModel model = holder.getPanelModel();
                Class<? extends Component> panelClass = holder.getPanelClass();
                if (model != null && panelClass.getConstructor(String.class, IModel.class) != null) {
                    return panelClass.getConstructor(String.class, IModel.class).newInstance(wid, model);
                } else {
                    return holder.getPanelClass().getConstructor(String.class).newInstance(wid);
                }
            } catch (Throwable t) {
                feedbackHolders.remove(holder);
                return null;
            }
        }
    }

    public FeedbackHolder addFeedbackPanel(Class<? extends Component> panelClass) {
        return addFeedbackPanel(panelClass, null);
    }

    public FeedbackHolder addFeedbackPanel(Class<? extends Component> panelClass, IModel model) {
        synchronized (feedbackHolders) {
            removeFeedbackPanel(panelClass);
            FeedbackHolder holder = new FeedbackHolder(panelClass, model);
            feedbackHolders.addLast(holder);
            return holder;
        }
    }

    public void removeFeedbackPanel(Class panelClass) {
        synchronized (feedbackHolders) {
            for (Iterator<FeedbackHolder> iterator = feedbackHolders.iterator(); iterator.hasNext(); ) {
                FeedbackHolder holder = iterator.next();
                if (holder.getPanelClass().equals(panelClass)) {
                    iterator.remove();
                }
            }
        }
    }

    public boolean hasFeedback() {
        return !getFeedbackMessages().isEmpty() || !feedbackHolders.isEmpty();
    }

    private class FeedbackHolder implements Serializable {

        private Class<? extends Component> panelClass;
        private IModel panelModel;

        public FeedbackHolder(Class<? extends Component> panelClass, IModel panelModel) {
            this.panelClass = panelClass;
            this.panelModel = panelModel;
        }

        public Class<? extends Component> getPanelClass() {
            return panelClass;
        }

        public IModel getPanelModel() {
            return panelModel;
        }
    }
}
