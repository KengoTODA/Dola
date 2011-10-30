package jp.skypencil.enchantjs.dola.command;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;

public class JoinStepTest {
	private File targetDir = Files.createTempDir();
	private File resultDir = Files.createTempDir();

	@Test
	public void test() throws IOException {
		for (int i = 0; i < 10; ++i) {
			byte b = (byte) ('0' + i);
			Files.write(new byte[] { b }, new File(targetDir, i + ".txt"));
		}
		new JoinStep(resultDir, targetDir, Charset.forName("UTF-8"), ".txt").run();
		String joined = Files.toString(new File(resultDir, "joined.txt"), Charset.forName("UTF-8"));
		assertThat(joined, is("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n"));
	}

	@Test
	public void testIgnoreOtherExt() throws IOException {
		for (int i = 0; i < 10; ++i) {
			byte b = (byte) ('0' + i);
			Files.write(new byte[] { b }, new File(targetDir, i + ".txt"));
		}
		for (int i = 0; i < 10; ++i) {
			byte b = (byte) ('0' + i);
			Files.write(new byte[] { b }, new File(targetDir, i + ".lst"));
		}
		new JoinStep(resultDir, targetDir, Charset.forName("UTF-8"), ".lst").run();
		String joined = Files.toString(new File(resultDir, "joined.lst"), Charset.forName("UTF-8"));
		assertThat(joined, is("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n"));
	}

	@Test
	public void testJoinGrandchildFiles() throws IOException {
		File childDir = new File(targetDir, "child.d");
		childDir.mkdir();
		for (int i = 0; i < 10; ++i) {
			byte b = (byte) ('0' + i);
			Files.write(new byte[] { b }, new File(childDir, i + ".txt"));
		}
		new JoinStep(resultDir, targetDir, Charset.forName("UTF-8"), ".txt").run();
		String joined = Files.toString(new File(resultDir, "joined.txt"), Charset.forName("UTF-8"));
		assertThat(joined, is("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n"));
	}

	@Test
	public void testEmbedText() throws IOException {
		Files.write("assets['1byte.txt']".getBytes(), new File(targetDir, "testEmbedText.js"));
		Files.copy(new File("src/test/resources/1byte.txt"), new File(targetDir, "1byte.txt"));
		new JoinStep(resultDir, targetDir, Charset.forName("UTF-8"), ".js").run();
		String joined = Files.toString(new File(resultDir, "joined.js"), Charset.forName("UTF-8"));
		assertThat(joined, is("assets['data:text/plain;base64,IQ==']\n"));
	}

	@Test
	public void testEmbedImage() throws IOException {
		Files.write("var image = assets[\"bar.png\"];".getBytes(), new File(targetDir, "testEmbedImage.js"));
		Files.copy(new File("src/test/resources/bar.png"), new File(targetDir, "bar.png"));
		new JoinStep(resultDir, targetDir, Charset.forName("UTF-8"), ".js").run();
		String joined = Files.toString(new File(resultDir, "joined.js"), Charset.forName("UTF-8"));
		assertThat(joined, is("var image = assets[\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAQCAYAAADXnxW3AAAACXBIWXMAAAsTAAALEwEAmpwYAAADDmlDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjajZPLbxtVFMZ/Y4+nlRKxwbTFqtAVizZCSTR9qE2EaGvHrpU2GGuaFCdCqibja3vIzXh6Z5w+1AXqhh0tiD3iIVb8ARXqgl1XKJWqgpAq1lQIVKmoG1TMwo+ZlkA5q3O/853vfPdcXch96oahygjYDGLtVEuisbomdv1EhleZoMCE60VhsV5fAnDDUPGPePIDBsC9mZ3r/xkTTRl5YOwGus3I2wTjMmTPe6GOwbwLzF2Kwxhyu4G8bqyuQW4/kG8PchvIrw/yMpDXy84C5BrAS17HbUKuA0yvp/B2Kh94ACBflYHUviecaknUdbflK5my+4Ly/4xN1RvNexmYDOOSAxwA/mjp0yvAFBiFq53ld4E9YJT8eHF5iDeC9do7A77xwUb3rDPkfN2U5QowDcbtaOtcZcS52lmoDTm/vO+eqQMFMP4K4/pQJ7MvULWlgX7mhIwq50Z4yz+9OMRboaovDfQzH+meswK8DplvXV2pDvkPZLAy7M1mmm75LDAL2SnqSDQtfBQChyolBFUkAQ/R+Hh0qaPpjlltHo6rF+khETicp3Sz0JtKVK7rC763fePRM93d1JQ2kgA5nlJNnSM2xswZwkThzrXb+xKN++at9+5N3rlGEYWijWQTiU+AJEoppDy0gpuFRMHbvvFIflh7UuP6dILaP9q/2fftz+2v7F//ZUft53ZUTM2XBDu6lyhWUWOHO7N8FD4rBPhcQqKJcFEEXEGkdzHqNl8zD5mL5pw5jzBPmqfME2bZnDPfNJdGDKtila0iwnrDmrcOWWesYnIT66BVtuatg1blmRfwUndNv6BMsWRqIzOsI4nYQOOzhUQRISGWl2OAhW54RfvtTiwO2/ZxUQxDJcVi4M1OC1cpof12J46ElpHUW7I5S2N1TQy+42MHAzD2bCdY/Da89Ttk7ybYWg++iWDv0QSbKsArn8GtY15Pbw3/t2F8D1HryOHBabIEuZ/7/ccHYNcn8PTjfv/PL/r9p19C9gF8p/4GqSMK1iULo98AAAAgY0hSTQAAbZgAAHOOAADyewAAhNoAAG6UAADlGgAAMycAABkXmUkcfwAAACtJREFUeNpiYEie+Z+JgYGBgYmBgYGBgWHPf2Ru5XtkLjYlzAysUg0ILmAANacLjrm5WjwAAAAASUVORK5CYII=\"];\n"));
	}
}
