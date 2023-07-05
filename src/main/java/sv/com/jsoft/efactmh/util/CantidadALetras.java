package sv.com.jsoft.efactmh.util;

import java.math.BigDecimal;

public class CantidadALetras {

    public static String aLetras(BigDecimal paramBigDecimal) {
        String str6 = "";

        String str8 = "";

        paramBigDecimal = paramBigDecimal.setScale(2, 4);

        String str1 = paramBigDecimal.toString();

        for (int i = 0; i <= 11 - str1.length(); i++) {
            str6 = str6 + "0";
        }
        str1 = str6 + str1;
        String str2 = str1.substring(10);
        String str3 = str1.substring(6, 9);
        String str4 = str1.substring(3, 6);
        String str5 = str1.substring(0, 3);

        if (!str5.equals("000")) {
            String str7 = analizaCadena(str5);

            if (Integer.parseInt(str5) != 1) {
                str8 = str7.trim() + " MILLONES ";
            } else {
                str8 = str7.trim() + " MILLON ";
            }
            if (Integer.parseInt(str4) != 0) {
                str7 = analizaCadena(str4);
                str8 = str8 + " " + str7.trim() + " MIL";
            }

            if (Integer.parseInt(str3) != 0) {
                str7 = analizaCadena(str3);
                str8 = str8 + " " + str7.trim();
            }

            str8 = str8 + " " + str2 + "/100";
            return str8;
        }

        if (!str4.equals("000")) {
            String str7 = analizaCadena(str4);
            str8 = str8 + " " + str7.trim() + " MIL";

            if (Integer.parseInt(str3) != 0) {
                str7 = analizaCadena(str3);
                str8 = str8 + " " + str7.trim();
            }

            str8 = str8 + " " + str2 + "/100";
            return str8;
        }

        if (!str3.equals("000")) {
            String str7 = analizaCadena(str3);
            str8 = str8 + " " + str7.trim();
            str8 = str8 + " " + str2 + "/100";
            return str8;
        }

        return str8;
    }

    public static String bLetras(BigDecimal paramBigDecimal) {
        String str6 = "";

        String str8 = "";

        paramBigDecimal = paramBigDecimal.setScale(2, 4);

        String str1 = paramBigDecimal.toString();

        for (int i = 0; i <= 11 - str1.length(); i++) {
            str6 = str6 + "0";
        }
        str1 = str6 + str1;

        String str3 = str1.substring(6, 9);
        String str4 = str1.substring(3, 6);
        String str5 = str1.substring(0, 3);

        if (!str5.equals("000")) {
            String str7 = analizaCadena(str5);

            if (Integer.parseInt(str5) != 1) {
                str8 = str7.trim() + " MILLONES ";
            } else {
                str8 = str7.trim() + " MILLON ";
            }
            if (Integer.parseInt(str4) != 0) {
                str7 = analizaCadena(str4);
                str8 = str8 + " " + str7.trim() + " MIL";
            }

            if (Integer.parseInt(str3) != 0) {
                str7 = analizaCadena(str3);
                str8 = str8 + " " + str7.trim();
            }

            return str8;
        }

        if (!str4.equals("000")) {
            String str7 = analizaCadena(str4);
            str8 = str8 + " " + str7.trim() + " MIL";

            if (Integer.parseInt(str3) != 0) {
                str7 = analizaCadena(str3);
                str8 = str8 + " " + str7.trim();
            }

            return str8;
        }

        if (!str3.equals("000")) {
            String str7 = analizaCadena(str3);
            str8 = str8 + " " + str7.trim();

            return str8;
        }

        return str8;
    }

    public static String analizaCadena(String paramString) {
        String str3 = "";

        String[] arrayOfString1 = {"", "UNO ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE "};
        String[] arrayOfString2 = {"", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS ", "DIECISIETE ", "DIECIOCHO ", "DIECINUEVE "};
        String[] arrayOfString3 = {"", "", "", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
        String[] arrayOfString4 = {"", "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};

        if (!paramString.substring(0, 1).equals("0")) {
            if (paramString.equals("100")) {
                String str4 = "CIEN";
                return str4;
            }

            str3 = arrayOfString4[Integer.parseInt(paramString.substring(0, 1))];

            if (paramString.substring(1).equals("00")) {
                String str4 = str3.trim();
                return str4;
            }

        }

        int i = paramString.charAt(1);
        String str2;
        String str4;
        switch (i) {
            case 48:
                str2 = arrayOfString1[Integer.parseInt(paramString.substring(2))];
                str4 = str3 + str2;
                break;
            case 49:
                if (paramString.substring(2).equals("0")) {
                    str2 = "DIEZ";
                } else {
                    str2 = arrayOfString2[Integer.parseInt(paramString.substring(2))];
                }
                str4 = str3 + str2;
                break;
            case 50:
                if (paramString.substring(2).equals("0")) {
                    str4 = str3 + " VEINTE";
                } else {
                    str2 = arrayOfString1[Integer.parseInt(paramString.substring(2))];
                    str4 = str3 + " VEINTI" + str2;
                }
                break;
            default:
                str2 = arrayOfString3[Integer.parseInt(paramString.substring(1, 2))];
                if (!paramString.substring(2).equals("0")) {
                    str2 = str2 + " Y " + arrayOfString1[Integer.parseInt(paramString.substring(2))];
                }
                str4 = str3 + " " + str2;
        }
        return str4;
    }
}
