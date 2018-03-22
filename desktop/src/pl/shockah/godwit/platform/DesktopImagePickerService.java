package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Pixmap;

import java.awt.EventQueue;
import java.io.File;
import java.nio.file.Files;

import javax.annotation.Nonnull;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.shockah.func.Action1;

public class DesktopImagePickerService extends ImagePickerService {
	@Override
	@Nonnull public PermissionState getPermissionState(@Nonnull Source source) {
		return PermissionState.Authorized;
	}

	@Override
	public boolean isCameraAvailable() {
		return false;
	}

	@Override
	public void requestPermission(@Nonnull Source source, @Nonnull Action1<PermissionState> newStateDelegate) {
		newStateDelegate.call(PermissionState.Authorized);
	}

	@Override
	public void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> pixmapDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
				throw new RuntimeException(e);
			}

			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					File file = chooser.getSelectedFile();
					byte[] bytes = Files.readAllBytes(file.toPath());
					pixmapDelegate.call(new Pixmap(bytes, 0, bytes.length));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}