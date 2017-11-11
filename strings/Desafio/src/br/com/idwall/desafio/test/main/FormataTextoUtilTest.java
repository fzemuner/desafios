package br.com.idwall.desafio.test.main;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.idwall.desafio.util.FormataTextoUtil;

/**
 * Classe de teste com cenários para a  FormataTextoUtil
 * @author fzemuner
 *
 */
public class FormataTextoUtilTest {

	/**
	 * testTextoComQuebra(): Apresenta texto com dois parágrafos
	 */
	@Test
	public void testTextoComQuebra() {
		String texto = "In the beginning God created the heavens and the earth. Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters." +
			   "\nAnd God said, \"Let there be light,\" and there was light. God saw that the light was good, and he separated the light from the darkness. God called the light \"day,\" and the darkness he called \"night.\" And there was evening, and there was morning - the first day.";
		
		assertPadrao(texto);
	}
	
	/**
	 * testPalavraMaiorQueLimiteComTextoAdicional(): Apresenta um texto sem espaços entre as palavras. Ao final, apresenta um pequeno texto com espaços entre as palavras
	 */
	@Test
	public void testPalavraMaiorQueLimiteComTextoAdicional() {
		String texto = "InthebeginningGodcreatedtheheavensandtheearth.Nowtheearthwasformlessandempty,darknesswasoverthesurfaceofthedeep,andtheSpiritofGodwashoveringoverthewaters. And God said,";
		
		assertPadrao(texto);
	}
	
	/**
	 * testPalavraMaiorQueLimiteComQuebra(): Apresenta um texto sem espaços entre as palavras. Ao final, apresenta um outro parágrafo com um pequeno texto com espaços entre as palavras
	 */
	@Test
	public void testPalavraMaiorQueLimiteComQuebra() {
		String texto = "InthebeginningGodcreatedtheheavensandtheearth.Nowtheearthwasformlessandempty,darknesswasoverthesurfaceofthedeep,andtheSpiritofGodwashoveringoverthewaters.\nAnd God said,";
		
		assertPadrao(texto);
	}
	
	/**
	 * testPalavrasMaioresQueLimiteComTextoAdicional(): O texto deste teste apresenta palavras com mais de 40 caracteres 
	 */
	@Test
	public void testPalavrasMaioresQueLimiteComTextoAdicional() {
		String texto = "InthebeginningGodcreatedtheheavensandthe earth.Nowtheearthwasformlessan dempty,darknesswasoverthesurfa ceofthedeep,andtheSpiritofGodwashovering overthewaters. And God said,";
		
		assertPadrao(texto);
	}
	
	/**
	 * testTextoSemQuebra():  Apresenta texto de um único parágrafo  
	 */
	@Test
	public void testTextoSemQuebra() {
		String texto = "In the beginning God created the heavens and the earth. Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters. " +
			   "And God said, \"Let there be light,\" and there was light. God saw that the light was good, and he separated the light from the darkness. God called the light \"day,\" and the darkness he called \"night.\" And there was evening, and there was morning - the first day.";
		
		assertPadrao(texto);
	}
	
	/**
	 * testTextoSemQuebra():  Apresenta texto com 3 parágrafos 
	 */
	@Test
	public void testTextoCom2Quebras() {
		String texto = "In the beginning God created the heavens and the earth. Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters." +
			   "\nAnd God said, \"Let there be light,\" and there was light."
			   + "\nGod saw that the light was good, and he separated the light from the darkness. God called the light \"day,\" and the darkness he called \"night.\" And there was evening, and there was morning - the first day.";
		
		assertPadrao(texto);
	}
	
	@Test
	public void testTextoVazio() {
		String texto = "";
		
		assertPadrao(texto);
	}
	
	@Test
	public void testTextoNulo() {
		String texto = null;
		
		assertPadrao(texto);
	}
	
	
	private void assertPadrao(String texto) {
		String textoFinal = FormataTextoUtil.formatarTexto(texto);
		
		System.out.println("--------------------------------------------------------");
		System.out.println(textoFinal);
		System.out.println("--------------------------------------------------------");
		
		assertTrue(validar(getLinhas(textoFinal)));
	}
	
	private String[] getLinhas(String texto) {
		return texto.split(FormataTextoUtil.QUEBRA);
	}
	
	private boolean validar(String[] linhas) {
		boolean resultado = true;
		for (int i=0; i < linhas.length; i++) {
			String linha = linhas[i];

			//linha tem menos que limite e próxima linha não tem caracter? OK, pois entende-se que fechou um parágrafo
			//Se a linha corrente não tem espaço e tem menos que 40 caracteres, OK tb; entende-se que pode fechar dessa forma por ser uma palavra muito grande. Mais elegante quebrar palavras em linhas
			//se linha tem zero caracteres, OK
			//se tem mais que o limite, NOK

			if (linha.length() < FormataTextoUtil.QTDE_CARACTERES) {
				if (linha.length() > 0 &&
						i <= linha.length() - 1 && 
						linhas[i+1].length() > 0 &&
						linha.indexOf(" ") >= 0) {
					resultado = false;
					break;
				} 
			} if (linha.length() > FormataTextoUtil.QTDE_CARACTERES) {
				resultado = false;
				break;
			}
		}
		
		return resultado;
	}
	
}
