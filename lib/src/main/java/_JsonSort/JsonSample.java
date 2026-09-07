package _JsonSort;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
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
			System.out.println("並べ替えたい項目はなんですか？");
			System.out.print("[0:番号 1:クラス 2:年齢 3:点数]＞");
			int item = sc.nextInt();//itemに並べたい項目の値を格納

			if(item >= 0 && item <= 3) {
				int sortdata = sortdata(item);//並替種別を取得
				if(sortdata == 0 || sortdata == 1) {
					jsonperse(item,sortdata);
				}else {
					System.out.println("");
					System.out.println("入力情報が不正です。");
				}
			}else {
				System.out.println("");
				System.out.println("入力情報が不正です。");
			}
	    //引数に「-help」以外がある場合そのまま終了	
        }else {
        	return;
        }	
	}
	
	//昇順降順情報を取得
	public  static int  sortdata(int item) {
		System.out.println("");
		System.out.println("並替種別を指定してください。");
		System.out.print("[0:昇順 1:降順]＞");
		int sorttype = sc.nextInt();//並替種別を格納する。
		if(sorttype == 0 || sorttype == 1) {
			return sorttype;
		}
    	return 2;
	}
	
	public static void jsonperse(int item,int type) {
		ObjectMapper mapper = new ObjectMapper();
        try {
            
            File jsonFile = new File("test.json");//取り込むjsonファイル名

            File outputFile = new File("result.json");//出力するjsonファイル名
            
            // 2. readValueにFileオブジェクトを直接渡してパース
            Result response = mapper.readValue(jsonFile, Result.class);
            System.out.println("");
            System.out.println("＜結果＞");
            printAligned("%-7s", "番号");
            printAligned("%-16s", "名前");
            printAligned("%-8s", "クラス");
            printAligned("%5s", "年齢");
            printAligned("%8s%n", "点数");
            
            // 3. リストを取得して出力確認
            List<Student> students = response.getDatas();
            
            if(item == 0 && type == 0) {
        		students.sort(Comparator.comparing(Student::getNo));
            }else if (item == 0 && type == 1){
            	students.sort(Comparator.comparing(Student::getNo).reversed());
            }else if (item == 1 && type == 0){
            	students.sort(Comparator.comparing(Student::getKurasu));
            }else if (item == 1 && type == 1){
            	students.sort(Comparator.comparing(Student::getKurasu).reversed());
            }else if (item == 2 && type == 0){
            	students.sort(Comparator.comparing(Student::getAge));
            }else if (item == 2 && type == 1){
            	students.sort(Comparator.comparing(Student::getAge).reversed());
            }else if (item == 3 && type == 0){
            	students.sort(Comparator.comparing(Student::getVal));
            }else if (item == 3 && type == 1){
            	students.sort(Comparator.comparing(Student::getVal).reversed());
            }
            for (Student student : students) {
      
                	    printAligned("%-7s", student.getNo());
                	    printAligned("%-16s", student.getName());
                	    printAligned("%-8s", student.getKurasu());
                	    printAligned("%5s", student.getAge() + "歳");
                	    printAligned("%8s%n", student.getVal() + "点");
            }

            // 4. 元のフォーマット（スペース1つのインデント）に合わせてJSONを出力する設定
            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
            DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(" ", DefaultIndenter.SYS_LF);
            printer.indentObjectsWith(indenter);
            printer.indentArraysWith(indenter);
            

            // 4. writeValue() を使用してオブジェクトをJSONファイルとして書き出し
            mapper.writer(printer).writeValue(outputFile, response);

            System.out.println("ファイルの保存が完了しました: " + outputFile.getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("ファイルの読み込み、またはパースに失敗しました。");
            e.printStackTrace();
        }
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
