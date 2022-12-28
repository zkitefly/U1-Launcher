/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2021  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jackhuang.hmcl.Metadata;
import org.jackhuang.hmcl.ui.construct.DialogCloseEvent;
import org.jackhuang.hmcl.upgrade.RemoteVersion;
import org.jackhuang.hmcl.util.io.HttpRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import static org.jackhuang.hmcl.ui.FXUtils.onEscPressed;
import static org.jackhuang.hmcl.util.Logging.LOG;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class UpgradeDialog extends JFXDialogLayout {
    public UpgradeDialog(RemoteVersion remoteVersion, Runnable updateRunnable) {
        {
            setHeading(new Label(i18n("update.changelog")));
        }

        {
            String url = Metadata.CHANGELOG_URL + remoteVersion.getChannel().channelName + ".html";
            try {
                WebView webView = new WebView();
                webView.getEngine().setUserDataDirectory(Metadata.HMCL_DIRECTORY.toFile());
                WebEngine engine = webView.getEngine();
                CompletableFuture.runAsync(() -> {
                    try {
                        String page = HttpRequest.GET(url).getString();
                        Platform.runLater(() -> engine.loadContent(page));
                    } catch (IOException e) {
                        LOG.log(Level.WARNING, "Failed to load update log, trying to open it in browser", e);
                        FXUtils.openLink(url);
                        setBody();
                    }
                });
                setBody(webView);
            } catch (NoClassDefFoundError | UnsatisfiedLinkError e) {
                LOG.log(Level.WARNING, "WebView is missing or initialization failed", e);
                FXUtils.openLink(url);
            }
        }

        {
            JFXButton updateButton = new JFXButton(i18n("update.accept"));
            updateButton.getStyleClass().add("dialog-accept");
            updateButton.setOnMouseClicked(e -> updateRunnable.run());

            JFXButton cancelButton = new JFXButton(i18n("button.cancel"));
            cancelButton.getStyleClass().add("dialog-cancel");
            cancelButton.setOnMouseClicked(e -> fireEvent(new DialogCloseEvent()));

            setActions(updateButton, cancelButton);
            onEscPressed(this, cancelButton::fire);
        }
    }
}
