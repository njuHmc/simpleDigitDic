import java.io.*;
import java.util.*;

public class Dic {

	private ArrayList<String> word = new ArrayList<String>();
	private ArrayList<String> explain = new ArrayList<String>();
	private int[] firstIndex = new int[26];
	
	public Dic() throws FileNotFoundException
	{
		File file = new File("dictionary.txt");
		if(!file.exists())
		{
			System.out.println("���ļ�ʧ�ܣ�");
			return;
		}
		Scanner input = new Scanner(file);
		int letterIndex = 0;
		firstIndex[letterIndex++] = 0;//��ĸa��ʼλ��Ϊ0
		
		while(input.hasNext())
		{
			String str = input.nextLine();
			String[] single = str.split("\t");
			if(word.size() != 0)
			{
				if((single[0].charAt(0) != word.get(word.size()-1).charAt(0)) && (Math.abs(single[0].charAt(0) - word.get(word.size()-1).charAt(0)) != 32))
					firstIndex[letterIndex++] = word.size() + 1;
			}
			word.add(single[0]);
			explain.add(single[1]);//�������ԭ�ַ������õ�ַ�����½���ַ������?
		}

		input.close();
	}
	
	public String[] getWord()
	{
		String[] str = word.toArray((new String[word.size()]));
		return str;
	}
	
	public int getNum()
	{
		return word.size();
	}
	
	private int different(String s1, String s2)//�ҵ������ַ�����һ����һ���ַ���λ��
	{
		int len = s1.length();
		if(s2.length() < len)
			len = s2.length();
		for(int i = 0; i < len ; i++)
			if(s1.charAt(i) != s2.charAt(i))
				return i;
		return len;
	}	
	
	public String search(String w)//����������ť�����õ���
	{
		String str = new String("���޴˴�");
		int l = w.charAt(0) - 'A';
		if(l > 25)
			l -= 32;
		int head = firstIndex[l];
		int tail;
		if(l!=25)
			tail = firstIndex[l+1] - 1;
		else
			tail = word.size() - 1;
		
		while(head <= tail)
		{
			int middle= (head + tail) / 2;
			int cha = w.compareToIgnoreCase(word.get(middle));
			
			while(Math.abs(cha) > 25)//���������ַ����µĲ�һ�����
			{
				int diff = different(w, word.get(middle));
				if(diff == w.length() || diff == word.get(middle).length())
					break;
				else
				{
					if((w.charAt(diff) == '\'')  || (w.charAt(diff) == '-'))//
						cha = w.substring(diff+1).compareToIgnoreCase(word.get(middle).substring(diff));
					else if((word.get(middle).charAt(diff) == '\'') || (word.get(middle).charAt(diff) == '-'))//
						cha = w.substring(diff).compareToIgnoreCase(word.get(middle).substring(diff+1));
					else if((word.get(middle).charAt(diff) == ' ') || (w.charAt(diff) == ' ') || (w.charAt(diff) == '/') || (word.get(middle).charAt(diff) == '/') || (w.charAt(diff) == '.') || (word.get(middle).charAt(diff) == '.'))
						break;
				}
			}
			
			if(cha == 0)
			{
				str=explain.get((head + tail) / 2).toString();
				break;
			}
			else if(cha < 0)
				tail = (head + tail) / 2 - 1;
			else if(cha > 0)
				head = (head + tail) / 2 + 1;
		}

		return str;
	}
	
	public String[] association(String prefix)//��������
	{
		String[] result;
		int head = 0, tail = 0, len = prefix.length();
		boolean find = false;
		for(int i = 0; i < word.size(); i++)
		{
			if(word.get(i).length() < len)
				continue;
			int dif = prefix.substring(0, len).compareToIgnoreCase(word.get(i).substring(0, len));

			while(dif > 25)//���������ַ����µĳ�������ƥ������
			{
				int di = different(prefix.substring(0, len), word.get(i).substring(0, len));
				if((prefix.charAt(di) == '\'')  || (prefix.charAt(di) == '-'))
				{
					if(di+1==len)
						break;
					else if(di+1==len-1)
						dif = prefix.charAt(di+1) - word.get(i).charAt(di);
					else
						dif = prefix.substring(di+1, len).compareToIgnoreCase(word.get(i).substring(di, len - 1));
				}
				else if((word.get(i).charAt(di) == '\'') || (word.get(i).charAt(di) == '-'))
				{
					if(word.get(i).length() == len)
						break;
					else if(di+1==len)
						dif = prefix.charAt(di) - word.get(i).charAt(di+1);
					else
						dif = prefix.substring(di, len).compareToIgnoreCase(word.get(i).substring(di+1, len + 1));
				}
				else if((word.get(i).charAt(di) == ' ') || (prefix.charAt(di) == ' ') || (prefix.charAt(di) == '/') || (word.get(i).charAt(di) == '/') || (prefix.charAt(di) == '.') || (word.get(i).charAt(di) == '.'))
					break;				
			}
			if(dif == 0)
			{
				head = i;
				find = true;
				break;
			}
		}
		
		if(find == false)//���о�����
		{
			int count = 0;
			for(int i = 0; i < word.size(); i++)
			{
				if(Math.abs(prefix.length() - word.get(i).length()) > 2)
					continue;
				if(edit(word.get(i), prefix) < 2)//�༭����Ϊ1�ľ������
					count++;
			}
			result = new String[count + 1];
			result[0] = "��Ҫ�ҵ��ǲ��ǣ�";
			for(int i = 0, j = 1; (i < word.size()) && (j < count + 1); i++)
				if(edit(word.get(i), prefix) < 2)
					result[j++] = word.get(i);
		}
		else if((head == word.size() - 1) && (find == true))//���⴦�����һ�����ʵ�����
		{
			result = new String[1];
			result[0] = word.get(head);
		}
		else
		{
			find = false;
			for(int i = head + 1; i < word.size(); i++)
			{
				if(word.get(i).length() < len && prefix.substring(0, word.get(i).length()).compareToIgnoreCase(word.get(i).substring(0, word.get(i).length())) == 0)
					continue;
				else if(word.get(i).length() < len && prefix.substring(0, word.get(i).length()).compareToIgnoreCase(word.get(i).substring(0, word.get(i).length())) != 0)
				{
					tail = i;
					find = true;
					break;
				}
				int dif = prefix.substring(0, len).compareToIgnoreCase(word.get(i).substring(0, len));
				while(dif > 25)
				{
					int di = different(prefix.substring(0, len), word.get(i).substring(0, len));
					if((prefix.charAt(di) == '\'')  || (prefix.charAt(di) == '-'))
					{
						if(di+1==len)
							break;
						else if(di+1==len-1)
							dif = prefix.charAt(di+1) - word.get(i).charAt(di);
						else
							dif = prefix.substring(di+1, len).compareToIgnoreCase(word.get(i).substring(di, len - 1));
					}
						
					else if((word.get(i).charAt(di) == '\'') || (word.get(i).charAt(di) == '-'))
					{
						if(word.get(i).length() == len)
							break;
						else if(di+1==len)
							dif = prefix.charAt(di) - word.get(i).charAt(di+1);
						else
							dif = prefix.substring(di, len).compareToIgnoreCase(word.get(i).substring(di+1, len + 1));
					}
					else if((word.get(i).charAt(di) == ' ') || (prefix.charAt(di) == ' ') || (prefix.charAt(di) == '/') || (word.get(i).charAt(di) == '/') || (prefix.charAt(di) == '.') || (word.get(i).charAt(di) == '.'))
						break;
				}
				if(dif != 0)
				{
					tail = i;
					find = true;
					break;
				}
			}
			if(find == false)
				tail = word.size();
			
			result = new String[tail - head];
			for(int i = head, j = 0; i < tail; i++, j++)
				result[j] = word.get(i);
		}
		return result;
	}
	
	private static int min(int a, int b)
	{
	    return a < b ? a : b;
	}

	private int edit(String str1, String str2)//��̬�滮�༭�����㷨
	{
	    int max1 = str1.length();
	    int max2 = str2.length();

	    int [][]ptr = new int[max1 + 1][max2 + 1];
/*	    for(int i = 0; i < max1 + 1 ;i++)
	    {
	        ptr[i] = new int[max2 + 1];
	    }*/

	    for(int i = 0; i < max1 + 1; i++)
	        ptr[i][0] = i;

	    for(int i = 0; i < max2 + 1; i++)
	    {
	        ptr[0][i] = i;
	    }

	    for(int i = 1; i < max1 + 1; i++)
	    {
	        for(int j = 1; j< max2 + 1; j++)
	        {
	            int d;
	            int temp = min(ptr[i-1][j] + 1, ptr[i][j-1] + 1);
	            if(str1.charAt(i - 1) == str2.charAt(j-1))
	                d = 0 ;
	            else
	                d = 1 ;
	            ptr[i][j] = min(temp, ptr[i-1][j-1] + d);
	        }
	    }

	    return Math.abs(ptr[max1][max2]);
	}

}
