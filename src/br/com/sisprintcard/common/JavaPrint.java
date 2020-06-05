////////////////////////////////////////////////////////////////////////////////
// Copyright (c) Datacard Corporation.  All Rights Reserved.
////////////////////////////////////////////////////////////////////////////////
package br.com.sisprintcard.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.Sides;

import net.sf.jasperreports.engine.type.OrientationEnum;

public class JavaPrint implements Printable {
	private final double POINTS_PER_INCH = 72;
	private String mPrinterName = null;
	private boolean mDuplex;
	private boolean mLandscape;
	private int mCopyCount;

	private String mMatricula = null;
	private String mNome = null;
	private String mTipoDependencia = null;
	private String mValidade = null;
	private String mVersao = null;
	private String mDataNascimento = null;
	private String mMunicipio = null;
	private String mOrgao = null;
	private String mTipoPlano = null;
	private String mCarencias1 = null;
	private String mCarencias2 = null;
	private String mCarencias3 = null;
	private Integer modelo;
	private String mMatricOrgao = null;

	public JavaPrint(String aPrinterName) {
		mPrinterName = aPrinterName;
		mDuplex = false;
		mCopyCount = 1;
	}

	public JavaPrint(String aPrinterName, boolean isDuplex, boolean isLandscape, int copyCount) {
		mPrinterName = aPrinterName;
		mDuplex = isDuplex;
		mLandscape = isLandscape;
		mCopyCount = copyCount;
	}

	public JavaPrint(String aPrinterName, boolean isDuplex, boolean isLandscape, boolean isJISMagstripe,
			int copyCount) {
		mPrinterName = aPrinterName;
		mDuplex = isDuplex;
		mLandscape = isLandscape;
		mCopyCount = copyCount;
	}

	public JavaPrint(String aPrinterName, String matricula, String nome, String tipoDependencia, String validade,
			String versao, String dataNascimento, String municipio, String orgao, String tipoPlano, String carencias_1,
			String carencias_2, String carencias_3, Integer model, String matricOrgao) {
		mPrinterName = aPrinterName;
		mDuplex = true;
		mLandscape = true;
		mCopyCount = 1;

		mMatricula = matricula;
		mNome = nome;
		mTipoDependencia = tipoDependencia;
		mValidade = validade;
		mVersao = versao;
		mDataNascimento = dataNascimento;
		mMunicipio = municipio;
		mOrgao = orgao;
		mTipoPlano = tipoPlano;
		mCarencias1 = carencias_1;
		mCarencias2 = carencias_2;
		mCarencias3 = carencias_3;
		modelo = model;
		mMatricOrgao = matricOrgao;
	}

	public JavaPrint(String aPrinterName, String matricula, String nome, String tipoDependencia, String validade,
			String versao, String dataNascimento, String municipio, String orgao, String tipoPlano, String carencias_1,
			String carencias_2, String carencias_3, Integer model) {
		mPrinterName = aPrinterName;
		mDuplex = true;
		mLandscape = true;
		mCopyCount = 1;

		mMatricula = matricula;
		mNome = nome;
		mTipoDependencia = tipoDependencia;
		mValidade = validade;
		mVersao = versao;
		mDataNascimento = dataNascimento;
		mMunicipio = municipio;
		mOrgao = orgao;
		mTipoPlano = tipoPlano;
		mCarencias1 = carencias_1;
		mCarencias2 = carencias_2;
		mCarencias3 = carencias_3;
		modelo = model;
	}

	public JavaPrint(String mPrinterName2, boolean b, boolean c, String string, boolean d, Object object,
			Object object2, boolean e) {
		// TODO Auto-generated constructor stub
	}

	private PrintService GetPrinterService() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

		for (PrintService printer : printServices) {
			if (printer.getName().compareToIgnoreCase(mPrinterName) == 0) {
				System.out.println("Found Printer: " + printer.getName());
				return printer;
			}
		}
		System.out.println("Did not match any printer name as : " + mPrinterName);
		return null;
	}

	public void Print() {
		PrintService printService = GetPrinterService();
		if (printService == null) {
			return;
		}

		try {
			PrinterJob printJob = PrinterJob.getPrinterJob();
			printJob.setPrintService(printService);

			// Set the printable class to this one since we are implementing the Printable
			// interface:
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

			if (mLandscape) {
				aset.add(OrientationRequested.LANDSCAPE);
			} else {
				aset.add(OrientationRequested.PORTRAIT);
			}

			aset.add(new MediaPrintableArea(0.0f, 0.0f, 53.98f, 85.6f, MediaSize.MM));
			aset.add(new Copies(mCopyCount));

			if (mDuplex) {
				aset.add(Sides.DUPLEX);
				mDuplex = true;
			} else {
				aset.add(Sides.ONE_SIDED);
			}

			printJob.setPrintable(this);
			printJob.print(aset);
		} catch (Exception PrintException) {
			PrintException.printStackTrace();
		}
	}

	/**
	 * Method: print
	 * <p>
	 * 
	 * This class is responsible for rendering a page using the provided parameters.
	 * The result will be a grid where each cell is one half inch square.
	 * 
	 * @param g          a value of type Graphics
	 * @param pageFormat a value of type PageFormat
	 * @param pageNumber a value of type int
	 * @return an int error code.
	 */
	public int print(Graphics g, PageFormat pageFormat, int pageNumber) {
		// --- Validate the page number, we only print max two pages
		if (pageNumber > 1) {
			System.out.println("Print job has been sent to spooler.");
			return Printable.NO_SUCH_PAGE;
		}

		// --- Translate the origin to be (0,0)
		Graphics2D graphics2D = (Graphics2D) g;

		graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		final int printerDPI = 300;

		double pageWidth = pageFormat.getWidth() * printerDPI / POINTS_PER_INCH;

		// Imprime a Frente do Cartão
		System.out.println("pageWidth: " + pageWidth);
		System.out.println("pageFormat: " + pageFormat.getWidth());
		if (pageNumber == 0) {
			if (modelo == 3) {
				pageFormat.setOrientation(OrientationEnum.PORTRAIT.getValue());
				graphics2D.setColor(Color.black);
				graphics2D.setFont(new Font(null, Font.BOLD, 12));
				graphics2D.drawString(mMatricula, (int) 12, (int) 100);
			} else if (modelo == 2) {
				// Para impressão cartão reciprocidade
				graphics2D.setColor(Color.black);
				graphics2D.setFont(new Font(null, Font.BOLD, 12));
				graphics2D.drawString(mMatricula, (int) 12, (int) 100);
				graphics2D.setFont(new Font(null, Font.BOLD, 10));
				graphics2D.drawString(mNome, (int) 12, (int) 110);
				graphics2D.setFont(new Font(null, Font.BOLD, 7));
				graphics2D.drawString("VALIDADE: " + mValidade, (int) 12, (int) 145);
			} else { // Impressão cartão normal if(modelo == 1)
				graphics2D.setColor(Color.black);
				graphics2D.setFont(new Font(null, Font.BOLD, 12));
				graphics2D.drawString(mMatricula, (int) 12, (int) 100);
				graphics2D.setFont(new Font(null, Font.BOLD, 10));
				graphics2D.drawString(mNome, (int) 12, (int) 110);
				graphics2D.setFont(new Font(null, Font.BOLD, 7));
				graphics2D.drawString(mTipoDependencia, (int) 12, (int) 120);
				graphics2D.drawString("VALIDADE: " + mValidade, (int) 12, (int) 135);
				graphics2D.drawString("Versão: " + mVersao, (int) 12, (int) 145);
			}

			return (Printable.PAGE_EXISTS);
		}
		// Imprime o Verso do Cartão
		else if (pageNumber == 1 && mDuplex) {
			if (modelo == 2) { // Para Impressão cartão reciprocidade
				graphics2D = (Graphics2D) g;
				graphics2D.setColor(Color.black);
				graphics2D.rotate(Math.toRadians(180));

				graphics2D.setFont(new Font(null, Font.BOLD, 8));
				// graphics2D.drawString("www.cassems.com.br - (67) 3314-1010", (int) -200,
				// (int) -145);

				graphics2D.setFont(new Font(null, Font.BOLD, 6));
				graphics2D.drawString("DATA NASC: " + mDataNascimento + " MUNICIPIO: " + mMunicipio, (int) -220,
						(int) -82);
				graphics2D.drawString("TIPO PLANO: " + mTipoPlano + "  Versão: " + mVersao, (int) -220, (int) -75);

				Line2D.Double line = new Line2D.Double();
				line.setLine(-240, -60, pageWidth, -60);
				// graphics2D.draw(line);

				graphics2D.drawString("INSC GEAP: " + mMatricOrgao, (int) -220, (int) -62);

				graphics2D.setFont(new Font(null, Font.BOLD, 6));
				graphics2D.drawString("URGÊNCIA/EMERGÊNCIA - CLINICA CAMPO GRANDE E HOSP. PENFIGO", (int) -220,
						(int) -55);

				graphics2D.drawString("Consultas em endocrinologia, geriatria, otorrinolaringologia, urologia,",
						(int) -220, (int) -48);
				graphics2D.drawString("reumatologia e Proctologia;  Procedimentos endoscópicos;", (int) -220,
						(int) -41);
				graphics2D.drawString("Outras necessidades, obter autorização diretamente com a GEAP/MS.", (int) -220,
						(int) -34);
			} else { // Impressão Cartão Normal if(modelo == 1)
				graphics2D = (Graphics2D) g;
				graphics2D.setColor(Color.black);
				graphics2D.rotate(Math.toRadians(180));

				graphics2D.setFont(new Font(null, Font.BOLD, 8));
				// graphics2D.drawString("www.cassems.com.br - (67) 3314-1010", (int) -200,
				// (int) -145);

				graphics2D.setFont(new Font(null, Font.BOLD, 6));
				// graphics2D.drawString("CNS: 1234567891011", (int) -220, (int) -90);
				graphics2D.drawString("DATA NASC: " + mDataNascimento + " MUNICIPIO: " + mMunicipio, (int) -220,
						(int) -82);
				graphics2D.drawString("ÓRGÃO: " + mOrgao, (int) -220, (int) -75);

				Line2D.Double line = new Line2D.Double();
				line.setLine(-240, -60, pageWidth, -60);
				// graphics2D.draw(line);

				graphics2D.drawString("TIPO PLANO: " + mTipoPlano, (int) -220, (int) -62);
				graphics2D.drawString("CARÊNCIAS:", (int) -220, (int) -55);
				graphics2D.drawString(mCarencias1, (int) -220, (int) -48);
				graphics2D.drawString(mCarencias2, (int) -220, (int) -41);
				graphics2D.drawString(mCarencias3, (int) -220, (int) -34);
			}
			return (Printable.PAGE_EXISTS);
		} else {
			return (NO_SUCH_PAGE);
		}
	}

	public void PrintDemo() {
	}

}
