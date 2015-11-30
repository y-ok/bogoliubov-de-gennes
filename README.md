# bogoliubov-de-gennes

## 特徴

本プログラムは、二次元光格子におけるトラップポテンシャル中のフェルミオンの状態をシミュレートします。  
特徴としては、以下の5項目のパラメータを制御することで、様々な物理量をシミュレートすることが可能です。

- ホッピングエネルギー
- 粒子間相互作用
- 粒子数インバランス
- トラップポテンシャルの強さ
- 温度

また、シミュレート出来る物理量としては以下の通りです。

- アップスピン粒子またはダウンスピン粒子の実空間における分布
- 超流動パラメータの実空間における分布
- 磁化の実空間における分布

## モデル

以下のBogoliubov-de Gennesハミルトニアンを平均場近似の範囲内で対角化を実施しています。

<img src="http://latex.codecogs.com/png.latex?%5Cdpi%7B0%7D%20%5Cbg_white%20%5Cdisplaystyle%7B%0A%5Chat%7B%5Ccal%20H%7D%20%3D%20-t%5Csum_%7B%7B%5Clangle%7Dij%7B%5Crangle%7D%5Csigma%7D%0A%5Cleft(%20%0A%5Chat%7Bc%7D%5E%5Cdagger_%7Bi%5Csigma%7D%0A%5Chat%7Bc%7D_%7Bj%5Csigma%7D%2B%5Crm%7BH.c.%7D%0A%5Cright)%0A%2B%5Csum_%7Bi%5Csigma%7D%0A%5Cleft(%0AV_%7Bi%7D-%0A%5Cmu_%7B%5Csigma%7D%0A%5Cright)%5Chat%7Bn%7D_%7Bi%5Csigma%7D%0A%2B%5Csum_%7Bi%7DU%0A%5Chat%7Bn%7D_%7Bi%5Cuparrow%7D%5E%5Cdagger%0A%5Chat%7Bn%7D_%7Bi%5Cdownarrow%7D%0A%7D">
