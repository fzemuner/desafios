package br.com.idwall.desafio.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.idwall.desafio.bean.RegistroBean;

public class Main {

	private static final int LIMITE_RELEVANTE = 5000;
	
	/**
	 * Navega nas threads das subreddits de https://www.reddit.com/r/$ para obter os upbotes, onde $ é a subreddit listado no args
	 *  
	 * @param args[0]: deve ser enviado uma lista de subreddits separado por ponto e vírgula. O valor default é "askreddit;worldnews;cats" 
	 * 
	 */
	public static void main(String[] args) {
		System.out.println("======================================INICIO PROCESSAMENTO============= " + Calendar.getInstance().getTime());
		
		//default subreddits
		//String strSubreddits = "askreddit;worldnews;cats";
		String strSubreddits = "askreddit;brazil;cats";
		
		//obterndo do parametro args
		if (args != null && args.length > 0 && args[0].length() > 0) {
			strSubreddits = args[0];
		}
		
		//cria lista de resultado (registroBeanList terá a lista de itens com upvotes relevantes)
		List<RegistroBean> registroBeanList = new ArrayList<RegistroBean>();
		//cria lista de subreddits
		String[] subreddits = 	strSubreddits.split(";");
		
		//instancia os browsers. Utilizado Chrome. Há um driver na raiz do projeto
		System.setProperty("webdriver.chrome.driver", "drive/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriver driverDetail = new ChromeDriver();
		
		for (String subreddit : subreddits) {
			System.out.println("==>SUBREDDIT: " + subreddit);
			System.out.println("======================================INICIO SUBREDDIT " + subreddit + " ============= " + Calendar.getInstance().getTime());
			List<WebElement> list = null;
			
			try {
				//acessando a subreddit 
				driver.get("https://www.reddit.com/r/" + subreddit);
				
				//obtém a lista de threads
				list = driver.findElements(ByClassName.className(("top-matter")));
			} catch (Exception e) {
				System.out.println("ERRO=>> incapaz de obter lista de threads por className 'top-matter' da subreddit " + subreddit);
				e.printStackTrace();
			}
						
			for (WebElement e : list) {
				String strDescTherad = "";
				try {
					//obtem o link da thread 
					WebElement a = e.findElement(ByTagName.tagName("a"));
					driverDetail.get(a.getAttribute("href"));
					strDescTherad = a.getText();
					
					WebElement number = null;
					int upVotes = 0;
					try {
						
						//obtem o numero de upvotes
						number = driverDetail.findElement(By.className("number"));
						upVotes = getUpVotes(number.getText());
						
					} catch (Exception erro) {
						//se der erro ao obter upvotes, desconsidera a thread
						System.out.println("UPVOTES NAO ENCONTRADO==>> " + a.getText());
					}
					
					//adiciona na lista se o upvotes for relevante
					if (upVotes >= LIMITE_RELEVANTE) {
						RegistroBean registroBean = new RegistroBean();
						registroBean.setSubreddit(subreddit);
						registroBean.setTitulo(a.getText());
						registroBean.setLink(a.getAttribute("href"));
						registroBean.setUpVotes(upVotes);
						registroBeanList.add(registroBean);
					}
					
				} catch (Exception erro2) {
					System.out.println("ERRO=>> incapaz de obter thread. | subreddit:  " + subreddit + " | thread: " + strDescTherad);
					erro2.printStackTrace();
				}
			}
		}

		driver.close();
		driverDetail.close();
		
		System.out.println("======================================INICIO     IMPRESSAO============= " + Calendar.getInstance().getTime());
		System.out.println();
		
		for (RegistroBean rb : registroBeanList) {
			System.out.println("==>| " + rb.getSubreddit() + " | " + rb.getUpVotes() + " | " + rb.getTitulo() + " | " + rb.getLink());
		}
		
		System.out.println();
		System.out.println("======================================FIM    PROCESSAMENTO============= " + Calendar.getInstance().getTime());
	}

	/**
	 * Transforma a string do upvotes em numero
	 */
	private static int getUpVotes(String strNumber) {
		int upVotes = 0;
		if (strNumber == null || strNumber.equals("")) { 
			return upVotes;
		}
		
		try {
			
			strNumber = strNumber.replace(",", "");
			upVotes = Integer.parseInt(strNumber);
			
		} catch (Exception e) {
			System.out.println("ERRO: upVotes não identificado: " + strNumber);
		}
		
		return upVotes;
	}

}
