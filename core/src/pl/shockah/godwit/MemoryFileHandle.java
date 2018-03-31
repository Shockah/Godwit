package pl.shockah.godwit;

import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Getter;

public class MemoryFileHandle extends FileHandle {
	@Getter
	protected byte[] bytes = null;

	@Override
	public InputStream read() {
		if (bytes == null)
			throw new IllegalStateException("Can't read from a MemoryFileHandle without writing to it first.");
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public OutputStream write(boolean append) {
		if (append)
			throw new UnsupportedOperationException("Appending to a MemoryFileHandle is not supported.");
		return new ByteArrayOutputStream() {
			@Override
			public void flush() throws IOException {
				super.flush();
				bytes = toByteArray();
			}

			@Override
			public void close() throws IOException {
				super.close();
				bytes = toByteArray();
			}
		};
	}
}