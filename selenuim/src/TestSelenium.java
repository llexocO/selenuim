import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.csvreader.CsvReader;
public class TestSelenium {
	private ArrayList<String[]> csvList;
    WebDriver driver;

	@Before
	public  void readCSV(){
        driver = new FirefoxDriver();

	 try {       
         csvList = new ArrayList<String[]>(); //存csv中的信息
         String csvFilePath = "J:/软件测试/info.csv";
         CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("SJIS"));    //һ�����������Ϳ�����      
             
         reader.readHeaders(); // 去标题
             
         while(reader.readRecord()){ //读信息
             csvList.add(reader.getValues());   
         }               
         reader.close();   

     } catch (Exception ex) {   
             System.out.println(ex);   
         }   
}
@Test
public void testSelenium(){
    for(int row=0;row<csvList.size();row++) {
        driver.get("http://www.ncfxy.com");
        //取出csv文件中的信息
        String name = csvList.get(row)[0];
        String mail = csvList.get(row)[1];

        //获取登录页面的元素
        WebElement username = driver.findElement(By.name("name"));
        WebElement pwd = driver.findElement(By.name("pwd"));
        WebElement sumit = driver.findElement(By.id("submit"));

        //填信息
        username.clear();
        username.sendKeys(name);
        pwd.clear();
        pwd.sendKeys(name.substring(4));
        sumit.click();

        //获取信息页面的邮箱值
        WebElement tableEle = driver.findElement(By.id("table-main"));
        String tableEleTextStr = tableEle.getText();
        String mailEle = tableEleTextStr.substring(tableEleTextStr.indexOf("箱") + 2, tableEleTextStr.indexOf("学") - 1);
        String id = tableEleTextStr.substring( tableEleTextStr.indexOf("号") + 2);
        System.out.print(id);
        boolean match = name.equals(id) && mail.equals(mailEle);
        assertEquals(true, match);
    }
}

    @After
    public void close(){
        driver.close();
    }
}
