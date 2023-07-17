package antifraud.validation.util

import java.util.*

class IpAddressUtil {
    companion object {
        private fun toNumeric(ip: String?): Long {
            if (ip == null) {
                return 0
            }
            val sc: Scanner = Scanner(ip).useDelimiter("\\.")
            val l: Long = ((sc.nextLong() shl 24) + (sc.nextLong() shl 16) + (sc.nextLong() shl 8) + sc.nextLong())
            sc.close()
            return l
        }

        var ipComparator: Comparator<String?> = Comparator { ip1, ip2 ->
            toNumeric(ip1).compareTo(toNumeric(ip2))
        }
    }
}