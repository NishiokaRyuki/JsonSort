package _JsonSort;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSample {

private static final Scanner sc = new Scanner(System.in);
	
	
	
	public static void main(String[] args) {
		// 引数に「-help」があるか判断
        if (args.length > 0 && args[0].equalsIgnoreCase("-help")) {
			System.out.println("アプリケーション「JsonSample」は、JSON形式のファイル内の情報を");
			System.out.println("入力キーを元に並べ替えて結果を表示します。");
            return;
        //引数に何もない場合
        }else if(args.length == 0) {
        	boolean isValid = false;
        	do {//数字以外が入力されたら再入力を求める
        		System.out.println("並べ替えたい項目はなんですか？");
        		System.out.print("[0:番号 1:クラス 2:年齢 3:点数]＞");
        		try {
            		int sortitem = sc.nextInt();//itemに並べたい項目の値を格納
					isValid = true;
        			if(sortitem >= 0 && sortitem <= 3) {
        				int sorttype = sortdata(sortitem);//並替種別を取得
        				if(sorttype == 0 || sorttype == 1) {
        					jsonparse(sortitem,sorttype);
        				}else {
        					System.out.println("");
        					System.out.println("入力情報が不正です。");
        				}	        	
        			}else {
        				System.out.println("");
        				System.out.println("入力情報が不正です。");
        			}
        			
        		}catch (InputMismatchException  e) {
        			System.out.println("");
        			System.out.println("数字を入力してください。");
        			System.out.println("");
        			sc.next();
        		}
        	} while(!isValid);
        	
	    //引数に「-help」以外がある場合そのまま終了	
        }else {
        	return;
        }	
	}
	
	//昇順降順情報を取得
	public  static int  sortdata(int sortitem) {
		boolean isValid = false;
		do {
		System.out.println("");
		System.out.println("並替種別を指定してください。");
		System.out.print("[0:昇順 1:降順]＞");

			try {
				int sorttype = sc.nextInt();//並替種別を格納
				if(sorttype == 0 || sorttype == 1) {
					return sorttype;
				}else {
					return sorttype;
				}
			}catch  (InputMismatchException  e) {
    			System.out.println("");
    			System.out.println("数字を入力してください。");
    			System.out.println("");
    			sc.next();
			}
		}while(!isValid);
    	return 2;
	}
	
	public static void jsonparse(int sortitem,int sorttype) {
		ObjectMapper mapper = new ObjectMapper();
        try {
            
            File jsonFile = new File("test.json");//取り込むjsonファイル名

            File outputFile = new File("result.json");//出力するjsonファイル名
            
            // 2. readValueにFileオブジェクトを直接渡してパース
            Result response = mapper.readValue(jsonFile, Result.class);

            List<Student> students = response.getDatas();
            displayJsonSort(students,sortitem,sorttype);
            
            outputJsonFile(mapper,outputFile,response);
            
        } catch (IOException e) {
            System.err.println("ファイルの読み込み、またはパースに失敗しました。");
            e.printStackTrace();
        }
	}
	//ソートを行うメソッド
	public static void sortStudents(List<Student> students, int item, int type) {
        Comparator<Student> comparator;
        switch (item) {
            case 0:  comparator = Comparator.comparing(Student::getNo); break;
            case 1:  comparator = Comparator.comparing(Student::getKurasu); break;
            case 2:  comparator = Comparator.comparing(Student::getAge); break;
            case 3:  comparator = Comparator.comparing(Student::getVal); break;
            default: comparator = Comparator.comparing(Student::getNo); break;
        }

        if (type == 1) {
            comparator = comparator.reversed();
        }
        students.sort(comparator);
	}
	//ソート結果を表示するメソッド
	public static void displayJsonSort(List<Student> students,int sortitem,int sorttype) {
        System.out.println("\n＜結果＞");
        printAligned("%-7s", "番号");
        printAligned("%-16s", "名前");
        printAligned("%-8s", "クラス");
        printAligned("%5s", "年齢");
        printAligned("%8s%n", "点数");
     
        // 3. リストを取得して出力確認      
        sortStudents(students,sortitem,sorttype);
        for (Student student : students) {
  
            	    printAligned("%-7s", student.getNo());
            	    printAligned("%-16s", student.getName());
            	    printAligned("%-8s", student.getKurasu());
            	    printAligned("%5s", student.getAge() + "歳");
            	    printAligned("%8s%n", student.getVal() + "点");
        }
	}
	
	//Jsonファイルを出力するメソッド
	public static void outputJsonFile(ObjectMapper mapper,File outputFile,Result response) throws IOException{
        // 4. 元のフォーマット（スペース1つのインデント）に合わせてJSONを出力する設定
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(" ", DefaultIndenter.SYS_LF);
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        

        // 4. writeValue() を使用してオブジェクトをJSONファイルとして書き出し
        mapper.writer(printer).writeValue(outputFile, response);

	}
	
	//結果出力する際にインデントがずれるため揃える
	public static void printAligned(String format, Object val) {
	    String str = String.valueOf(val);
	    // 全角文字を2文字分、半角文字を1文字分として正確なバイト数を計算
	    int byteLength = 0;
	    for (char c : str.toCharArray()) {
	        byteLength += (c <= '\u007F') ? 1 : 2;
	    }
	    // 指定されたフォーマットの幅（数字）を取得
	    int targetWidth = Integer.parseInt(format.replaceAll("[^0-9]", ""));
	    // ズレた分のスペースを計算して補正
	    int padding = targetWidth - byteLength + str.length();
	    
	    // 新しいフォーマットを組み立てて出力
	    String newFormat = format.replaceAll("[0-9]+", String.valueOf(padding));
	    System.out.printf(newFormat, val);
	}

}