package br.com.idwall.desafio.util;

import java.util.ResourceBundle;

/**
 * Classe responsável por foratar o texto baseado na quantidade máxima de caracteres por linha
 * 
 * @author fzemuner
 *
 */
public class FormataTextoUtil {

	/**
	 * Quantidade caracteres do properties do projeto. Caso não tenha properties, pega o QTDE_CARACTERES_DEFAULT
	 * 
	 * @see getPropertieQtdeCaracteres
	 * @see QTDE_CARACTERES_DEFAULT
	 */
	public static final int QTDE_CARACTERES = getPropertieQtdeCaracteres();
	
	/**
	 * Quantidade caracteres padrão = 40
	 */
	private static final int QTDE_CARACTERES_DEFAULT =40;
	
	/**
	 * Caracter de quebra de linha. Se não tem no properties, pega a QUEBRA_DEFAULT
	 * 
	 * @see getPropertieQuebraLinha()
	 * @see QUEBRA_DEFAULT
	 */
	public static final String QUEBRA = getPropertieQuebraLinha();
	
	/**
	 * Caracter padrão de quebra de linha = \n
	 */
	public static final String QUEBRA_DEFAULT = "\n";

	
	/**
	 * Formata o texto baseado na quantidade de caracteres máxima por linha
	 * 
	 * @param texto: String à ser formatada
	 */
	public static String formatarTexto(String texto) {
		return formatarTexto(texto, QTDE_CARACTERES);
	}
	
	/**
	 * Formata o texto baseado na quantidade de caracteres máxima por linha
	 * 
	 * @param texto: String à ser formatada
	 * @param pQuebraCaracteres: Qtde de caracteres máximo por linha
	 */
	public static String formatarTexto(String texto, int pQuebraCaracteres) {
		if (texto == null || texto.equals("")) {
			return "";
		}
		
		StringBuffer textoFinal = new StringBuffer();
		try {
			
			String[] paragrafos = texto.split(QUEBRA);
			boolean primeiroParagrafo = true;
			
			for (String paragrafo : paragrafos) {
				if (!primeiroParagrafo) {
					textoFinal.append(QUEBRA);
				}
				primeiroParagrafo = false;
				
				StringBuffer sbLinha = new StringBuffer();
				String[] palavras = paragrafo.split(" ");
				boolean primeiraPalavraLinha = true;
				for (String palavra : palavras) {
					if (sbLinha.length() + palavra.length() + 1 <= pQuebraCaracteres) {	//se a palavra cabe na frase com o espaço anterior
						if (!primeiraPalavraLinha) {
							sbLinha.append(" ");
						}
						sbLinha.append(palavra);
						primeiraPalavraLinha = false;
					} else if (palavra.length() > pQuebraCaracteres) { //se a palavra for maior que o limite da linha, quebra a palavra no limite
						int qtdeLinhasQuebra = palavra.length() /  pQuebraCaracteres;
						if (palavra.length() %  pQuebraCaracteres > 0) qtdeLinhasQuebra++;
						for (int i=0; i < qtdeLinhasQuebra; i++) {
							if (i > 0 ) textoFinal.append(QUEBRA);
							if ((i+1) * pQuebraCaracteres -1 <= palavra.length()) { //Se tiver mais caracteres que o limite, quebra
								textoFinal.append(palavra.substring(i * pQuebraCaracteres, (i + 1) * pQuebraCaracteres));
							} else { //Se tiver menos que o limite, insere
								sbLinha.append(palavra.substring(i * pQuebraCaracteres, palavra.length()));
								primeiraPalavraLinha = false;
							}
						}
						continue;
					} else {
						textoFinal.append(justificar(sbLinha.toString(), pQuebraCaracteres)).append(QUEBRA);
						sbLinha = new StringBuffer();
						sbLinha.append(palavra);
					}
				}
				if (sbLinha.length() > 0) { //fecha a linha justificando
					textoFinal.append(justificar(sbLinha.toString(), pQuebraCaracteres)).append(QUEBRA);
				}
			}
		
		} catch (Exception e) {
			System.out.println("Um erro inesperado ocorreu ao processar o método formatarTexto.");
			e.printStackTrace();
		}
		
		return textoFinal.toString();
		
	}

	/**
	 * Justifica o texto complementando com espaços em branco até o limite de caracteres
	 * 
	 * @param linha : texto à justificar
	 * @param pQuebraCaracteres : Qtde de caracteres máximo por linha 
	 * @return : texto justificado
	 */
	private static String justificar(String linha, int pQuebraCaracteres) {
		StringBuffer sb = new StringBuffer();
		if (linha == null || linha.equals("")) return "";
		try {
			String[] palavras = linha.split(" ");
			int espacos = pQuebraCaracteres - linha.length();
			int qtdePalavras = palavras.length - 1;
			
			if (qtdePalavras == 0) {
				return linha;
			}
			
			int qtdeEspacosEntrePalavras = espacos / qtdePalavras;
			int qtdeEspacosFaltantes = pQuebraCaracteres - linha.length() - (qtdeEspacosEntrePalavras * qtdePalavras);
			
			//Cria o pool de espaços para distribuir entre as palavras
			StringBuffer sbEspacos = new StringBuffer(" ");
			for (int i=0; i < qtdeEspacosEntrePalavras; i++) {
				sbEspacos.append(" ");
			}
			String strEspacos = sbEspacos.toString();
			
			//adicionando espaços entre as palavras
			for (int i=0; i < palavras.length-1; i++) {
				palavras[i] = palavras[i] + strEspacos;
				
				//adicionando espaço do mod do cálculo para justificar
				if (i < qtdeEspacosFaltantes) {
					palavras[i] = palavras[i] + " ";
				}
			}
					
			//recompondo string
			for (String s : palavras) {
				sb.append(s);
			}
			
		} catch (Exception e) {
			System.out.println("Um erro inesperado ocorreu ao processar o método justificar.");
			e.printStackTrace();
		}
			
		return sb.toString();
	}

	/**
	 * Obtém a propriedade "quantidadeCaracteresFormatacao" do arquivo parametros.properties
	 * 
	 * @return quantidade de caracteres máxima para formatacao
	 */
	private static int getPropertieQtdeCaracteres() {
		int returnValue = QTDE_CARACTERES_DEFAULT;
		try {
			
			ResourceBundle rb = ResourceBundle.getBundle("parametros");
			String qtde = rb.getString("quantidadeCaracteresFormatacao");
			
			returnValue = Integer.parseInt(qtde);
			
		} catch (ClassCastException e) {
			System.out.println("Incapaz de ler parametro quantidadeCaracteresFormatacao, pois parametro não é número. Determinado valor default: " + QTDE_CARACTERES);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Incapaz de ler parametro quantidadeCaracteresFormatacao. Determinado valor default: " + QTDE_CARACTERES);
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	/**
	 * Obtém a propriedade "quebraLinha" do arquivo parametros.properties
	 * 
	 * @return caracter quebra linha
	 */
	private static String getPropertieQuebraLinha() {
		String returnValue = QUEBRA_DEFAULT;
		try {
			
			ResourceBundle rb = ResourceBundle.getBundle("parametros");
			returnValue = rb.getString("quebraLinha");
			
		} catch (ClassCastException e) {
			System.out.println("Incapaz de ler parametro quebraLinha, pois parametro não é número. Determinado valor default: " + QUEBRA_DEFAULT);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Incapaz de ler parametro quebraLinha. Determinado valor default: " + QUEBRA_DEFAULT);
			e.printStackTrace();
		}
		
		return returnValue;
	}

}
